import React from 'react';
import {Form, Message} from "semantic-ui-react";
import {useForm} from "react-hook-form";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {apiClient} from "../App";
import {GridWithOutput} from "../generated/api";

type GridAddFormData = {
    name: string
    age: number
}

type GridAddFormProps = {
    grids: Array<GridWithOutput>
    onFinished?: () => void
}

export const GridAddForm: React.FunctionComponent<GridAddFormProps> = ({grids, onFinished = () => {}}) => {
    const { register, handleSubmit, formState: { errors } } = useForm<GridAddFormData>();

    const queryClient = useQueryClient()

    const mutationParams = React.useMemo(() => ({
        mutationFn: (formData: GridAddFormData) => {
            return apiClient.loadNetwork({grid: [...grids, formData]})
        },
        onSuccess: () => {
            queryClient.invalidateQueries({predicate: () => true}).then(onFinished);
        },
    }), [queryClient, grids, onFinished]);

    const mutation = useMutation(mutationParams);

    const onSubmit = React.useCallback((formData: any) => {
        mutation.mutate(formData);
    }, [mutation]);

    const isUnique = React.useCallback((name: string) => {
        return !grids.some(i => i.name === name)
    }, [grids]);

    return (
        <Form onSubmit={handleSubmit(onSubmit)}>
            {mutation.isError && <Message negative>Error adding grid</Message>}

            <Form.Field error={errors.name}>
                <label>Name</label>
                <input placeholder='Amsterdam' {...register('name', {required: true, validate: isUnique})} />
                {errors.name && errors.name.type === "required" && <span>Name is required</span>}
                {errors.name && errors.name.type === "validate" && <span>Name is not unique</span> }
            </Form.Field>
            <Form.Field error={errors.age}>
                <label>Age</label>
                <input type='number' placeholder='0' {...register('age', {required: true, min: 0, max: 365 * 25})} />
                {errors.age && errors.age.type === "required" && <span>Age is required</span>}
                {errors.age && errors.age.type === "min" && <span>Age must be positive</span> }
                {errors.age && errors.age.type === "max" && <span>Age is too high</span> }
            </Form.Field>

            <Form.Button>Add grid</Form.Button>
        </Form>
    )
}