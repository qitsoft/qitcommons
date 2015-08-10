describe("Test q-switch panel.", function() {
    var $compile, $rootScope;

    beforeEach(function() {
        module("qitcommonsModule");

        inject(function(_$compile_, _$rootScope_) {
            $compile = _$compile_;
            $rootScope = _$rootScope_;
        });
    });

    it("Adds content of panel without value attribute.", function() {
        var element = $compile('<div><q-switch><panel><div class="inner-panel">Hello</div></panel></q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".inner-panel");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("panel .inner-panel");
        expect(innerPanel.length).toEqual(0);
    });

    it("Adds content of panel without value attribute if exists panel with value.", function() {
        var element = $compile('<div><q-switch><panel value="2"><div class="with-value-2">With value 2</div></panel><panel><div class="default">Default</div></panel></q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("panel .default");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);
    });

    it("Adds content of panel without value attribute if exists panel with value and switch value is specified.", function() {
        var element = $compile('<div><q-switch value="1">' +
            '<panel value="2"><div class="with-value-2">With value 2</div></panel>' +
            '<panel><div class="default">Default</div></panel>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("panel .default");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);
    });

    it("Adds content of panel by value.", function() {
        var element = $compile('<div><q-switch value="2"><panel value="2"><div class="with-value-2">With value 2</div></panel><panel><div class="default">Default</div></panel></q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("panel .with-value-2");
        expect(innerPanel.length).toEqual(0);
    });

    it("Adds content of multiple panels by value.", function() {
        var element = $compile('<div><q-switch value="2">' +
            '<panel value="2"><div class="with-value-2">With value 2</div></panel>' +
            '<panel value="2"><div class="with-value-2-a">With value 2a</div></panel>' +
            '<panel><div class="default">Default</div></panel>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-2-a");
        expect(innerPanel.length).toEqual(1);

        expect(element.text()).toEqual("With value 2With value 2a");
    });

    it("Adds content of multiple default panels.", function() {
        var element = $compile('<div><q-switch value="1">' +
            '<panel value="2"><div class="with-value-2">With value 2</div></panel>' +
            '<panel value="2"><div class="with-value-2-a">With value 2a</div></panel>' +
            '<panel><div class="default-1">Default 1</div></panel>' +
            '<panel><div class="default-2">Default 2</div></panel>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default-1");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".default-2");
        expect(innerPanel.length).toEqual(1);

        expect(element.text()).toEqual("Default 1Default 2");
    });

    it("Switches runtime panels.", function() {
        $rootScope._value = 1;
        var element = $compile('<div><q-switch value="_value">' +
            '<panel value="1"><div class="with-value-1">With value 1</div></panel>' +
            '<panel value="2"><div class="with-value-2">With value 2</div></panel>' +
            '<panel><div class="default">Default</div></panel>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-1");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        $rootScope._value = 2;
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-1");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        $rootScope._value = 3;
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-1");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(1);
    });

});