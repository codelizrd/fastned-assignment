import React from 'react';
import {GridWithOutput} from "../generated/api";
import {useToggle} from "../util";
import {Button, Confirm} from "semantic-ui-react";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {apiClient} from "../App";
import {ButtonProps} from "semantic-ui-react/dist/commonjs/elements/Button/Button";

type GridDeleteButtonProps = {
    grids: Array<GridWithOutput>
    toDelete: string
}

export const GridDeleteButton: React.FunctionComponent<GridDeleteButtonProps & Omit<ButtonProps, 'onClick'>> = ({grids, toDelete, ...buttonProps}) => {
    const toggle = useToggle(false);

    const queryClient = useQueryClient()

    const mutationParams = React.useMemo(() => ({
        mutationFn: () => {
            return apiClient.loadNetwork({grid: grids.filter(i => i.name !== toDelete)})
        },
        onSuccess: () => {
            queryClient.invalidateQueries({predicate: () => true}).then(toggle.off);
        },
    }), [queryClient, grids, toDelete, toggle.off]);

    const mutation = useMutation(mutationParams);

    const onConfirm = React.useCallback(() => mutation.mutate(), [mutation]);

    return (
        <>
            <Button onClick={toggle.on} {...buttonProps}>Delete</Button>
            <Confirm
                header='Confirm deletion'
                content={`Do you really want to delete grid "${toDelete}"`}
                open={toggle.value}

                onCancel={toggle.off}
                onConfirm={onConfirm}
            />
        </>
    );


}