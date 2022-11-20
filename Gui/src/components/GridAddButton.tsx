import React from 'react';
import {Button, Modal} from "semantic-ui-react";
import {useToggle} from "../util";
import {GridAddForm} from "./GridAddForm";
import {GridWithOutput} from "../generated/api";
import {ButtonProps} from "semantic-ui-react/dist/commonjs/elements/Button/Button";

type GridAddButtonProps = {
    grids: Array<GridWithOutput>
}

export const GridAddButton: React.FunctionComponent<GridAddButtonProps & Omit<ButtonProps, 'onClick'> & React.PropsWithChildren>
    = ({grids, children, ...buttonProps}) => {
    const toggle = useToggle(false);

    return (
        <Modal
            onClose={toggle.off}
            onOpen={toggle.on}
            open={toggle.value}
            trigger={<Button {...buttonProps}>{children}</Button>}>
            <Modal.Header>Add a new grid</Modal.Header>
            <Modal.Content>
                <GridAddForm grids={grids} onFinished={toggle.off}/>
            </Modal.Content>
        </Modal>
    );
}