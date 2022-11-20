import React from 'react';
import {Button, Modal} from "semantic-ui-react";
import {GridLoadForm} from "./GridLoadForm";
import {useToggle} from "../util";
import {ButtonProps} from "semantic-ui-react/dist/commonjs/elements/Button/Button";

export const GridLoadButton: React.FunctionComponent<Omit<ButtonProps, 'onClick'> & React.PropsWithChildren>
    = ({children, ...buttonProps}) => {
    const toggle = useToggle(false);

    return (
        <Modal
            onClose={toggle.off}
            onOpen={toggle.on}
            open={toggle.value}
            trigger={<Button {...buttonProps}>{children}</Button>}>
            <Modal.Header>Upload new network</Modal.Header>
            <Modal.Content>
                <GridLoadForm onFinished={toggle.off}/>
            </Modal.Content>
        </Modal>
    );
}