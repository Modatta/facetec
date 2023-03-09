'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var core = require('@capacitor/core');

const Facetec = core.registerPlugin('Facetec', {
    web: () => Promise.resolve().then(function () { return web; }).then(m => new m.FacetecWeb()),
});

class FacetecWeb extends core.WebPlugin {
    async echo(options) {
        console.log('ECHO', options);
        return options;
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    FacetecWeb: FacetecWeb
});

exports.Facetec = Facetec;
//# sourceMappingURL=plugin.cjs.js.map
