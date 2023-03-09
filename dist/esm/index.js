import { registerPlugin } from '@capacitor/core';
const Facetec = registerPlugin('Facetec', {
    web: () => import('./web').then(m => new m.FacetecWeb()),
});
export * from './definitions';
export { Facetec };
//# sourceMappingURL=index.js.map