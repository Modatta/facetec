import { registerPlugin } from '@capacitor/core';

import type { FacetecPlugin } from './definitions';

const Facetec = registerPlugin<FacetecPlugin>('Facetec');

export * from './definitions';
export { Facetec };
