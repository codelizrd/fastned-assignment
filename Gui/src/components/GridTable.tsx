import React from 'react';
import {GridWithOutputStatusEnum} from "../generated/api";
import {Button, Dimmer, Icon, Loader, Message, Table} from "semantic-ui-react";
import {GridDeleteButton} from "./GridDeleteButton";
import {GridAddButton} from "./GridAddButton";
import {GridLoadButton} from "./GridLoadButton";
import {Toggle} from "../util";
import {useQuery} from "@tanstack/react-query";
import {apiClient} from "../App";

type GridTableProps = {
    elapsedDays: number
    editToggle: Toggle
}

const getStatusIcon = (status: GridWithOutputStatusEnum) => {
    switch(status) {
        case GridWithOutputStatusEnum.Planned: return <Icon name='handshake' color='grey' />
        case GridWithOutputStatusEnum.Installing: return <Icon name='wrench' color='orange' />
        case GridWithOutputStatusEnum.Production: return <Icon name='sun' loading color='yellow' />
        case GridWithOutputStatusEnum.Decommissioned: return <Icon name='broken chain' color='red' />
    }
}

export const GridTable: React.FunctionComponent<GridTableProps> = ({editToggle, elapsedDays}) => {
    const queryFn = React.useCallback(() => apiClient.getNetworkAtElapsedDays({elapsedDays}), [elapsedDays]);
    const {isLoading, isPreviousData, isError, data} = useQuery({queryKey: ['network', elapsedDays], queryFn, keepPreviousData: true});

    if (data?.length === 0) {
        return (
            <Message>
                <p>No network defined yet</p>
                <GridAddButton size='small' grids={[]}>Add first grid</GridAddButton>
                <GridLoadButton size='small'>Load JSON file</GridLoadButton>
            </Message>
        )
    }

    return (
        <div>
            <Dimmer active={isLoading || isPreviousData} inverted>
                <Loader inverted>Loading network</Loader>
            </Dimmer>

            {isError && <Message negative>Could not load network</Message>}

            <Table striped color='yellow'>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell colSpan={editToggle.value ? 6 : 5} verticalAlign='middle'>
                            Network
                            {editToggle.value ? (
                                <span style={{float: 'right'}}>
                                    <Button onClick={editToggle.off} positive={editToggle.value}>Done editing</Button>
                                </span>
                            ) : (
                                <span style={{float: 'right'}}>
                                    <Button onClick={editToggle.on}>Edit</Button>
                                    <GridLoadButton>Load JSON</GridLoadButton>
                                </span>
                            )}
                        </Table.HeaderCell>
                    </Table.Row>
                    <Table.Row>
                        <Table.HeaderCell>Name</Table.HeaderCell>
                        <Table.HeaderCell textAlign='right'>Age</Table.HeaderCell>
                        <Table.HeaderCell textAlign='right'>Total output</Table.HeaderCell>
                        <Table.HeaderCell textAlign='right'>Day output</Table.HeaderCell>
                        <Table.HeaderCell textAlign='right'>Max peak output</Table.HeaderCell>
                        {editToggle.value && <Table.HeaderCell><GridAddButton size='small' positive fluid grids={data || []}>Add</GridAddButton></Table.HeaderCell>}
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {(data || []).map(grid => (
                        <Table.Row key={grid.name}>
                            <Table.Cell>
                                {getStatusIcon(grid.status)}
                                {' '}
                                {grid.name}
                            </Table.Cell>
                            <Table.Cell textAlign='right'>{grid.age.toLocaleString('en')} days</Table.Cell>
                            <Table.Cell textAlign='right'>{grid.totalOutputInKwh.toLocaleString('en')} kWh</Table.Cell>
                            <Table.Cell textAlign='right'>{grid.dailyOutputInKwh.toLocaleString('en', {maximumFractionDigits: 1, minimumFractionDigits: 1})} kWh</Table.Cell>
                            <Table.Cell textAlign='right'>{grid.peakOutputInKw.toLocaleString('en', {maximumFractionDigits: 1, minimumFractionDigits: 1})} kW</Table.Cell>
                            {editToggle.value && <Table.Cell><GridDeleteButton size='small' negative fluid grids={data || []} toDelete={grid.name}>Delete</GridDeleteButton></Table.Cell>}
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table>
        </div>
    );
}
