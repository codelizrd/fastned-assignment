import React from 'react';

export type Toggle = {
    value: boolean,
    toggle: (event?: React.MouseEvent | React.KeyboardEvent | any) => void,
    set: (value: boolean | ((current: boolean) => boolean)) => void,
    off: () => void,
    on: () => void
}

export function useToggle(initialState: boolean | (() => boolean) = false, preventDefault: boolean = true): Toggle {
    const [value, setValue] = React.useState<boolean>(initialState);

    return React.useMemo(() => ({
        value: value,

        toggle: (event) => {
            setValue((current) => !current);
            if (preventDefault && event && event.preventDefault) {
                event.preventDefault();
            }
        },

        set: (value) => setValue(value),

        off: () => setValue(false),

        on: () => setValue(true)

    }), [value, setValue, preventDefault]);
}

export const debounce = (fn: Function, ms = 300) => {
    let timeoutId: ReturnType<typeof setTimeout>;
    return function (this: any, ...args: any[]) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => fn.apply(this, args), ms);
    };
};

export const styling = {
    fontFamily: "Lato,'Helvetica Neue',Arial,Helvetica,sans-serif"
}