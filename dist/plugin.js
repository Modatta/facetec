var capacitorFacetec = (function (exports, core) {
    'use strict';

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

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
