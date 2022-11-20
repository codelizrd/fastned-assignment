import React from 'react';
import {GridTable} from "../components/GridTable";
import {NetworkStats} from "../components/NetworkStats";
import 'rc-slider/assets/index.css';
import {ElapsedDaysSelector} from "../components/ElapsedDaysSelector";
import {NetworkPeakOutputChart} from "../components/NetworkPeakOutputChart";
import { Segment } from 'semantic-ui-react';

const defaultElapsedDays = 0;
const maxDays = 30*365;

export const NetworkPage: React.FunctionComponent = () => {
    const [elapsedDays, setElapsedDays] = React.useState<number>(defaultElapsedDays);

    return (
        <>
            <GridTable elapsedDays={elapsedDays} />

            <Segment.Group>
                <ElapsedDaysSelector defaultValue={defaultElapsedDays} max={maxDays} onChange={setElapsedDays} />
                <NetworkStats elapsedDays={elapsedDays} />
            </Segment.Group>

            <NetworkPeakOutputChart highlightDay={elapsedDays} numberDays={maxDays} />
        </>
    )
}
