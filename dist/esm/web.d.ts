import { WebPlugin } from '@capacitor/core';
import type { FacetecPlugin } from './definitions';
export declare class FacetecWeb extends WebPlugin implements FacetecPlugin {
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
}
