import React from 'react';
import {Form, Grid, Message} from "semantic-ui-react";
import {useForm} from "react-hook-form";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {apiClient} from "../App";
import {ReadInputFile} from "./ReadInputFile";


const isValidJson = (value: string) => {
    try {
        JSON.parse(value);
        return true;
    } catch (error) {
        return false;
    }
}

type GridLoadFormData = {
    json: string
}

type GridLoadFormProps = {
    onFinished?: () => void
}

export const GridLoadForm: React.FunctionComponent<GridLoadFormProps> = ({onFinished = () => {}}) => {
    const { register, handleSubmit, formState: { errors }, setValue, trigger } = useForm<GridLoadFormData>();

    const queryClient = useQueryClient()

    const mutationParams = React.useMemo(() => ({
        mutationFn: (formData: GridLoadFormData) => {
            return apiClient.loadNetwork({grid: JSON.parse(formData.json)})
        },
        onSuccess: () => {
            queryClient.invalidateQueries({predicate: () => true}).then(onFinished);
        },
    }), [queryClient, onFinished]);

    const mutation = useMutation(mutationParams);

    const onSubmit = React.useCallback((formData: any) => {
        mutation.mutate(formData);
    }, [mutation]);

    const onJsonRead = React.useCallback((json: string) => {
        setValue('json', json);
        trigger().then()
    }, [setValue, trigger]);

    return (
        <Form onSubmit={handleSubmit(onSubmit)}>
            {mutation.isError && <Message negative>Error loading document</Message>}

            <Form.Field error={!!errors.json}>
                <label>Network in JSON format</label>
                <textarea placeholder='[{name: "Amsterdam", age: 85}]' {...register('json', {required: 'JSON is required', validate: isValidJson})} />
            </Form.Field>

            <Grid>
                <Grid.Column floated='left' width={8}>
                    <Form.Button>Load this network</Form.Button>
                </Grid.Column>
                <Grid.Column floated='right' width={8} textAlign='right'>
                    <ReadInputFile onRead={onJsonRead}>Import from file</ReadInputFile>
                </Grid.Column>
            </Grid>
        </Form>
    )
}