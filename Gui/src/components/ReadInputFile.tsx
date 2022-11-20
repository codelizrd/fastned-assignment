import React, {ChangeEventHandler, PropsWithChildren} from 'react';
import {Form} from "semantic-ui-react";
import {ButtonProps} from "semantic-ui-react/dist/commonjs/elements/Button/Button";

type ReadInputFileProps = {
    onRead: (text: string) => void
}

export const ReadInputFile: React.FunctionComponent<ReadInputFileProps & Omit<ButtonProps, 'onClick'> & PropsWithChildren> = ({onRead, children, ...buttonProps}) => {
    const fileRef = React.useRef<HTMLInputElement>(null);

    const onLoadFromFile = React.useCallback(() => {
        if (!!fileRef.current) {
            fileRef.current.click()
        }
    }, [fileRef]);

    const onSelectFile: ChangeEventHandler<HTMLInputElement> = React.useCallback((event) => {
        event.preventDefault()
        const reader = new FileReader()
        reader.onload = () => {
            const text = reader.result as string
            if (!!text) {
                onRead(text)
            }
        };
        reader.readAsText(event.target!!.files!![0])
    }, [onRead]);

    return (
        <>
            <Form.Button onClick={onLoadFromFile} {...buttonProps}>{children}</Form.Button>
            <input type='file' onChange={onSelectFile} ref={fileRef} style={{display: 'none'}}/>
        </>
    )
}