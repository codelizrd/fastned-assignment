import React, {ChangeEventHandler} from 'react';
import {Grid, Segment, Form} from "semantic-ui-react";
import Slider from "rc-slider";
import {debounce} from "../util";

type ElapsedDaysSelectorProps = {
    onChange: (days: number) => void
    defaultValue: number
    max: number | undefined
    disabled: boolean
}

export const ElapsedDaysSelector: React.FunctionComponent<ElapsedDaysSelectorProps> = ({defaultValue, max, onChange, disabled}) => {
    const [days, setDays] = React.useState<number>(defaultValue);

    // Reset to 0 upon change of 'disabled' attribute
    React.useEffect(() => {
        setDays(0)
    }, [disabled, setDays]);

    const debouncePropagator = React.useMemo(
        () => debounce((value: any) => onChange(value), 200
    ), [onChange]);

    const onSliderChange = React.useCallback((number: any) => {
        setDays(number)
        debouncePropagator(number)
    }, [setDays, debouncePropagator]);

    const onInputChange: ChangeEventHandler<HTMLInputElement>  = React.useCallback((event: any) => {
        setDays(event.target.value)
        debouncePropagator(event.target.value || 0)
    }, [setDays, debouncePropagator]);

    return (
            <Segment color='yellow'>
                <Form size='big'>
                    <Form.Field>
                        <label>Elapsed days</label>
                        <Grid verticalAlign='middle'>
                            <Grid.Column width='13'>
                                <Slider disabled={disabled} value={days} min={0} max={max || 30*365} onChange={onSliderChange}/>
                            </Grid.Column>
                            <Grid.Column width='3'>
                               <input type='number' disabled={disabled} value={days} onChange={onInputChange} style={{textAlign: 'center'}} />
                            </Grid.Column>
                        </Grid>
                    </Form.Field>
                </Form>
            </Segment>
    );
}