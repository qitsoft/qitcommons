describe("Test qUnit angular service.", function () {
    beforeEach(module("qitcommonsModule"));

    var $qUtils;
    beforeEach(inject(function (_$qUtils_) {
        $qUtils = _$qUtils_;
    }));

    describe("Test bool service method.", function () {
        it("Parse string", function () {
            expect($qUtils.bool("true")).toBe(true);
            expect($qUtils.bool("yes")).toBe(true);
            expect($qUtils.bool("on")).toBe(true);
            expect($qUtils.bool("t")).toBe(true);
            expect($qUtils.bool("y")).toBe(true);
            expect($qUtils.bool("TRUE")).toBe(true);
            expect($qUtils.bool("YES")).toBe(true);
            expect($qUtils.bool("ON")).toBe(true);
            expect($qUtils.bool("T")).toBe(true);
            expect($qUtils.bool("Y")).toBe(true);
            expect($qUtils.bool("TrUe")).toBe(true);
            expect($qUtils.bool("YeS")).toBe(true);
            expect($qUtils.bool("oN")).toBe(true);
            expect($qUtils.bool("1")).toBe(true);
            expect($qUtils.bool("false")).toBe(false);
            expect($qUtils.bool("no")).toBe(false);
            expect($qUtils.bool("off")).toBe(false);
            expect($qUtils.bool("f")).toBe(false);
            expect($qUtils.bool("n")).toBe(false);
            expect($qUtils.bool("FALSE")).toBe(false);
            expect($qUtils.bool("NO")).toBe(false);
            expect($qUtils.bool("OFF")).toBe(false);
            expect($qUtils.bool("FaLsE")).toBe(false);
            expect($qUtils.bool("nO")).toBe(false);
            expect($qUtils.bool("OfF")).toBe(false);
            expect($qUtils.bool("F")).toBe(false);
            expect($qUtils.bool("N")).toBe(false);
            expect($qUtils.bool("0")).toBe(false);
        });

        it("Parse number", function() {
            expect($qUtils.bool(0)).toBe(false);
            expect($qUtils.bool(1)).toBe(true);
            expect($qUtils.bool(100)).toBe(true);
            expect($qUtils.bool(0.0)).toBe(false);
            expect($qUtils.bool(1.5)).toBe(true);
            expect($qUtils.bool(100.5)).toBe(true);
            expect($qUtils.bool(Number.NaN)).toBe(false);
            expect($qUtils.bool(Number.NEGATIVE_INFINITY)).toBe(false);
            expect($qUtils.bool(Number.POSITIVE_INFINITY)).toBe(false);
            expect($qUtils.bool(Number.MAX_VALUE)).toBe(true);
            expect($qUtils.bool(Number.MIN_VALUE)).toBe(true);
        });

        it("Parse boolean", function(){
            expect($qUtils.bool(true)).toBe(true);
            expect($qUtils.bool(false)).toBe(false);
        });

        it("Null is false value", function() {
           expect($qUtils.bool(null)).toBe(false);
        });

        it("Undefined is false value", function() {
           expect($qUtils.bool(undefined)).toBe(false);
        });

        it("Object is true value", function() {
           expect($qUtils.bool({})).toBe(true);
        });

        it("DOMElement is false value", function() {
           expect($qUtils.bool(document.createElement)).toBe(false);
        });

        it("Parse array", function() {
           expect($qUtils.bool([])).toBe(false);
           expect($qUtils.bool(['aa'])).toBe(true);
        });

        it("Parse string with default value", function () {
            expect($qUtils.bool("true", false)).toBe(true);
            expect($qUtils.bool("yes", false)).toBe(true);
            expect($qUtils.bool("on", false)).toBe(true);
            expect($qUtils.bool("t", false)).toBe(true);
            expect($qUtils.bool("y", false)).toBe(true);
            expect($qUtils.bool("TRUE", false)).toBe(true);
            expect($qUtils.bool("YES", false)).toBe(true);
            expect($qUtils.bool("ON", false)).toBe(true);
            expect($qUtils.bool("T", false)).toBe(true);
            expect($qUtils.bool("Y", false)).toBe(true);
            expect($qUtils.bool("TrUe", false)).toBe(true);
            expect($qUtils.bool("YeS", false)).toBe(true);
            expect($qUtils.bool("oN", false)).toBe(true);
            expect($qUtils.bool("1", false)).toBe(true);
            expect($qUtils.bool("false", true)).toBe(false);
            expect($qUtils.bool("no", true)).toBe(false);
            expect($qUtils.bool("off", true)).toBe(false);
            expect($qUtils.bool("f", true)).toBe(false);
            expect($qUtils.bool("n", true)).toBe(false);
            expect($qUtils.bool("FALSE", true)).toBe(false);
            expect($qUtils.bool("NO", true)).toBe(false);
            expect($qUtils.bool("OFF", true)).toBe(false);
            expect($qUtils.bool("FaLsE", true)).toBe(false);
            expect($qUtils.bool("nO", true)).toBe(false);
            expect($qUtils.bool("OfF", true)).toBe(false);
            expect($qUtils.bool("F", true)).toBe(false);
            expect($qUtils.bool("N", true)).toBe(false);
            expect($qUtils.bool("0", true)).toBe(false);
        });

        it("Parse number with default value", function() {
            expect($qUtils.bool(0, true)).toBe(false);
            expect($qUtils.bool(1, false)).toBe(true);
            expect($qUtils.bool(100, false)).toBe(true);
            expect($qUtils.bool(0.0, true)).toBe(false);
            expect($qUtils.bool(1.5, false)).toBe(true);
            expect($qUtils.bool(100.5, false)).toBe(true);
            expect($qUtils.bool(Number.NaN, true)).toBe(false);
            expect($qUtils.bool(Number.NEGATIVE_INFINITY, true)).toBe(false);
            expect($qUtils.bool(Number.POSITIVE_INFINITY, true)).toBe(false);
            expect($qUtils.bool(Number.MAX_VALUE, false)).toBe(true);
            expect($qUtils.bool(Number.MIN_VALUE, false)).toBe(true);
        });

        it("Parse boolean with default value", function(){
            expect($qUtils.bool(true, false)).toBe(true);
            expect($qUtils.bool(false, true)).toBe(false);
        });

        it("Null is default value", function() {
            expect($qUtils.bool(null, false)).toBe(false);
            expect($qUtils.bool(null, true)).toBe(true);
        });

        it("Undefined is default value", function() {
            expect($qUtils.bool(undefined, false)).toBe(false);
            expect($qUtils.bool(undefined, true)).toBe(true);
        });

        it("Parse Object with default value", function() {
            expect($qUtils.bool({}, true)).toBe(true);
            expect($qUtils.bool({}, false)).toBe(true);
        });

        it("Parse DOMElement with default value", function() {
            expect($qUtils.bool(document.createElement, false)).toBe(false);
            expect($qUtils.bool(document.createElement, true)).toBe(true);
        });

        it("Parse array with default value", function() {
            expect($qUtils.bool([], true)).toBe(false);
            expect($qUtils.bool(['aa'], false)).toBe(true);
        });

    });

    it("Generate uuid", function() {
       expect($qUtils.uuid()).not.toBeNull();
       expect($qUtils.uuid()).toMatch(/[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[0-9a-f]{4}-[0-9a-f]{12}/i);
    });
});