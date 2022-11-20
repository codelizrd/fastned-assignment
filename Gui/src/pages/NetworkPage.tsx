import React from 'react';
import {GridTable} from "../components/GridTable";
import {NetworkStats} from "../components/NetworkStats";
import 'rc-slider/assets/index.css';
import {ElapsedDaysSelector} from "../components/ElapsedDaysSelector";
import {NetworkPeakOutputChart} from "../components/NetworkPeakOutputChart";
import { Segment } from 'semantic-ui-react';
import {useToggle} from "../util";

const defaultElapsedDays = 0;
const maxDays = 30*365;

export const NetworkPage: React.FunctionComponent = () => {
    const editToggle = useToggle(false);
    const [elapsedDays, setElapsedDays] = React.useState<number>(defaultElapsedDays);

    // If edit-mode is activated, set elapsed days to zero
    React.useEffect(() => {
        if (editToggle.value) {
            setElapsedDays(0)
        }
    }, [setElapsedDays, editToggle.value])

    return (
        <>
            <GridTable elapsedDays={elapsedDays} editToggle={editToggle} />

            <Segment.Group>
                <ElapsedDaysSelector disabled={editToggle.value} defaultValue={defaultElapsedDays} max={maxDays} onChange={setElapsedDays} />
                <NetworkStats elapsedDays={elapsedDays} />
            </Segment.Group>

            <NetworkPeakOutputChart highlightDay={elapsedDays} numberDays={maxDays} />
        </>
    )
}
