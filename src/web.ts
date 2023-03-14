import { WebPlugin } from '@capacitor/core';

import { Facetec } from '.';
import type { FacetecPlugin } from './definitions';

export class FacetecWeb extends WebPlugin implements FacetecPlugin {
  setup(): Promise<{ value: string }> {
    return Facetec.setup();
  }
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
