import React from 'react'
import {Dimmer, Loader, Message, Segment, Statistic} from 'semantic-ui-react'
import {useQuery} from "@tanstack/react-query";
import {apiClient} from "../App";

type NetworkStatsProps = {
    elapsedDays: number
}

export const NetworkStats: React.FunctionComponent<NetworkStatsProps> = ({elapsedDays}) => {

    const queryFn = React.useCallback(() => apiClient.getNetworkTotalOutputAtElapsedDays({elapsedDays}), [elapsedDays]);
    const {isLoading, isPreviousData, isError, data} = useQuery({queryKey: ['stats', elapsedDays], queryFn, keepPreviousData: true});

    return (
        <Segment>
            <Dimmer active={isLoading || isPreviousData} inverted>
                <Loader inverted>Loading network</Loader>
            </Dimmer>

            {isError && <Message negative>Could not load network</Message>}

            <Statistic.Group widths='three' size='small'>
                <Statistic>
                    <Statistic.Value>{data?.totalOutputInKwh.toLocaleString('en')} kWh</Statistic.Value>
                    <Statistic.Label>Total output</Statistic.Label>
                </Statistic>

                <Statistic>
                    <Statistic.Value>{data?.dailyOutputInKwh.toLocaleString('en', {maximumFractionDigits: 1})} kWh</Statistic.Value>
                    <Statistic.Label>Total output on day {elapsedDays}</Statistic.Label>
                </Statistic>

                <Statistic>
                    <Statistic.Value>{data?.peakOutputInKw.toLocaleString('en')} kW</Statistic.Value>
                    <Statistic.Label>Max peak during day {elapsedDays}</Statistic.Label>
                </Statistic>
            </Statistic.Group>
        </Segment>
    )
}