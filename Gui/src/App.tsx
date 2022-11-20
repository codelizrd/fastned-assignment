import React from 'react';
import 'semantic-ui-css/semantic.min.css'
import {QueryClientProvider, QueryClient} from '@tanstack/react-query'
import {Configuration, NetworkApi} from "./generated/api";
import {NetworkPage} from "./pages/NetworkPage";


export const apiClient = new NetworkApi(new Configuration({basePath: '/solar-simulator'}));
const queryClient = new QueryClient()

const App: React.FunctionComponent = () => {

    return (
        <QueryClientProvider client={queryClient}>
            <div style={{margin: '10px auto 10px auto', padding: '15px 0 100px 0', maxWidth: 960}}>
                <div>
                    <h1>☀️ Solar Charging Simulator</h1>
                </div>
                <NetworkPage />
            </div>
        </QueryClientProvider>
    );
}

export default App;
