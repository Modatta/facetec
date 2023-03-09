import { registerPlugin } from '@capacitor/core';

import type { FacetecPlugin } from './definitions';

const Facetec = registerPlugin<FacetecPlugin>('Facetec', {
  web: () => import('./web').then(m => new m.FacetecWeb()),
});

export * from './definitions';
export { Facetec };
