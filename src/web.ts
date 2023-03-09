import { WebPlugin } from '@capacitor/core';

import type { FacetecPlugin } from './definitions';

export class FacetecWeb extends WebPlugin implements FacetecPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
