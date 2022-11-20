import React from 'react';
import {apiClient} from "../App";
import {useQuery} from "@tanstack/react-query";
import {Dimmer, Loader, Message, Segment} from "semantic-ui-react";
import {Chart, LinearScale, CategoryScale, PointElement, LineElement, ChartOptions, Title, Tooltip, Filler} from "chart.js";
import {styling} from "../util";
import annotationPlugin from 'chartjs-plugin-annotation';
import {Line} from 'react-chartjs-2';

Chart.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Filler, annotationPlugin);

type NetworkPeakOutputChartProps = {
    numberDays: number
    highlightDay: number | undefined
}

const stepDays = 7

const chartOptions: ChartOptions<'line'> = {
    responsive: true,
    elements: {
        point: {
            radius: 0
        }
    },
    scales: {
        y: {
            type: 'linear',
            display: true,
            title: {
                display: true,
                text: 'Output in kWh',
                font: {
                    family: styling.fontFamily
                }
            }
        },
        x: {
            type: 'linear',
            display: true,
            title: {
                display: true,
                text: 'Elapsed day',
                font: {
                    family: styling.fontFamily
                }
            }
        }
    },
    plugins: {
        title: {
            display: true,
            text: 'Daily output',
            font: {
                family: styling.fontFamily
            }
        },
        tooltip: {
            enabled: true,
            bodyFont: {
                family: styling.fontFamily
            },
            mode: 'index',
            intersect: false,
        },
        filler: {
            propagate: false,
        },
        annotation: {
            annotations: {
                line: {
                    type: 'line',
                    borderColor: '#96dbfa',
                    borderWidth: 2,
                    scaleID: 'x',
                    value: 0,
                }
            }
        }
    }
}

export const NetworkPeakOutputChart: React.FunctionComponent<NetworkPeakOutputChartProps>
    = ({numberDays, highlightDay}) => {
    const queryFn = React.useCallback(() => apiClient.getNetworkDailyOutputAtElapsedDays({
        elapsedDays: numberDays,
        stepDays
    }), [numberDays]);
    const {isLoading, isError, data} = useQuery({queryKey: ['chart', numberDays], queryFn});

    const chartData = React.useMemo(() => {
        const labels = [];
        for (let i = 0; i <= numberDays; i = i + stepDays) labels.push(i)

        return {
            labels,
            datasets: [
                {
                    data: data,
                    borderColor: 'rgb(255, 99, 132)',
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    fill: 'start'
                }
            ]
        }
    }, [data, numberDays]);

    const chartRef = React.useRef<Chart>(null);

    // Move today annotation
    React.useEffect(() => {
        if (!chartRef.current) return
        // @ts-ignore
        chartRef.current.config.options.plugins.annotation.annotations.line.value = highlightDay;
        chartRef.current.update()
    }, [chartRef, highlightDay]);

console.log({chartData, chartOptions});

    return (
        <Segment color='yellow'>
            <Dimmer active={isLoading} inverted>
                <Loader inverted>Loading network</Loader>
            </Dimmer>

            {isError && <Message negative>Could not load network</Message>}

            <Line
                // @ts-ignore
                ref={chartRef}
                data={chartData}
                options={chartOptions}
            />
        </Segment>
    )

}