describe("Test q-switch panel.", function() {
    var $compile, $rootScope, $q;
    var testModule;

    beforeEach(function() {
        testModule = angular.module("TestQSwitchModule", []);
        testModule.controller("TestController", function($scope){
            $scope.value = 1;

            $scope.$on("changeValue", function(event, newValue) {
               $scope.value = newValue;
            });
        });

        module("qitcommonsModule");
        module("TestQSwitchModule");

        inject(function(_$compile_, _$rootScope_, _$q_) {
            $compile = _$compile_;
            $rootScope = _$rootScope_;
            $q = _$q_;
        });
    });

    it("Adds content of panel with empty value attribute.", function() {
        var element = $compile('<div><q-switch><q-switch-case value=""><div class="inner-panel">Hello</div></q-switch-case></q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".inner-panel");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("q-switch-case .inner-panel");
        expect(innerPanel.length).toEqual(1);
    });

    it("Adds content of panel with empty value attribute if exists panel with value.", function() {
        var element = $compile('<div><q-switch>\n' +
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>\n' +
            '<q-switch-case value=""><div class="default">Default</div></q-switch-case>\n' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("q-switch-case .default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find("q-switch-case .with-value-2");
        expect(innerPanel.length).toEqual(0);
    });

    it("Adds content of panel with empty value attribute if exists panel with value and switch value is specified.", function() {
        var element = $compile('<div><q-switch value="1">' +
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
            '<q-switch-case value=""><div class="default">Default</div></q-switch-case>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("q-switch-case .default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find("q-switch-case .with-value-2");
        expect(innerPanel.length).toEqual(0);
    });

    it("Adds content of panel with 'default' value attribute.", function() {
        var element = $compile('<div><q-switch><q-switch-case value="default"><div class="inner-panel">Hello</div></q-switch-case></q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".inner-panel");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("q-switch-case .inner-panel");
        expect(innerPanel.length).toEqual(1);
    });

    it("Adds content of panel with 'default' value attribute if exists panel with value.", function() {
        var element = $compile('<div><q-switch>' +
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
            '<q-switch-case value="default"><div class="default">Default</div></q-switch-case>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("q-switch-case .default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find("q-switch-case .with-value-2");
        expect(innerPanel.length).toEqual(0);
    });

    it("Adds content of panel with 'default' value attribute if exists panel with value and switch value is specified.", function() {
        var element = $compile('<div><q-switch value="1">' +
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
            '<q-switch-case value="default"><div class="default">Default</div></q-switch-case>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("q-switch-case .default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find("q-switch-case .with-value-2");
        expect(innerPanel.length).toEqual(0);
    });

    it("Adds content of panel with no value attribute.", function() {
        var element = $compile('<div><q-switch><q-switch-case><div class="inner-panel">Hello</div></q-switch-case></q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".inner-panel");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("q-switch-case .inner-panel");
        expect(innerPanel.length).toEqual(1);
    });

    it("Adds content of panel with no value attribute if exists panel with value.", function() {
        var element = $compile('<div><q-switch>' +
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
            '<q-switch-case><div class="default">Default</div></q-switch-case>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("q-switch-case .default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find("q-switch-case .with-value-2");
        expect(innerPanel.length).toEqual(0);
    });

    it("Adds content of panel with no value attribute if exists panel with value and switch value is specified.", function() {
        var element = $compile('<div><q-switch value="1">' +
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
            '<q-switch-case><div class="default">Default</div></q-switch-case>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("q-switch-case .default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find("q-switch-case .with-value-2");
        expect(innerPanel.length).toEqual(0);
    });

    it("Adds content of panel by value.", function() {
        var element = $compile('<div><q-switch value="2">' +
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
            '<q-switch-case><div class="default">Default</div></q-switch-case>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find("q-switch-case .with-value-2");
        expect(innerPanel.length).toEqual(1);
    });

    it("Adds content of panel by boolean false value.", function() {
        var element = $compile('<div><q-switch value="false">' +
            '<q-switch-case value="false"><div class="with-value-false">With value false</div></q-switch-case>' +
            '<q-switch-case value="true"><div class="with-value-true">With value true</div></q-switch-case>' +
            '<q-switch-case><div class="default">Default</div></q-switch-case>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-false");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-true");
        expect(innerPanel.length).toEqual(0);
    });

    it("Adds content of panel by boolean true value.", function() {
        var element = $compile('<div><q-switch value="true">' +
            '<q-switch-case value="false"><div class="with-value-false">With value false</div></q-switch-case>' +
            '<q-switch-case value="true"><div class="with-value-true">With value true</div></q-switch-case>' +
            '<q-switch-case><div class="default">Default</div></q-switch-case>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-false");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-true");
        expect(innerPanel.length).toEqual(1);
    });

    it("Adds content of multiple panels by value.", function() {
        var element = $compile('<div><q-switch value="2">' +
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
            '<q-switch-case value="2"><div class="with-value-2-a">With value 2a</div></q-switch-case>' +
            '<q-switch-case><div class="default">Default</div></q-switch-case>' +
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
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
            '<q-switch-case value="2"><div class="with-value-2-a">With value 2a</div></q-switch-case>' +
            '<q-switch-case value=""><div class="default-1">Default 1</div></q-switch-case>' +
            '<q-switch-case value=""><div class="default-2">Default 2</div></q-switch-case>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default-1");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".default-2");
        expect(innerPanel.length).toEqual(1);

        expect(element.text()).toEqual("Default 1Default 2");
    });

    it("Adds content of panel by value of q-switch-case attribute.", function() {
        var element = $compile('<div><q-switch value="2">' +
            '<div q-switch-case="2" class="with-value-2">With value 2</div>' +
            '<div class="default">Default</div>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(1);
    });

    it("Adds content of default panel if exists a panel with q-switch-case attribute.", function() {
        var element = $compile('<div><q-switch value="3">' +
            '<div q-switch-case="2" class="with-value-2">With value 2</div>' +
            '<div q-switch-case class="default">Default</div>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);
    });

    it("Switches runtime panels.", function() {
        $rootScope._value = 1;
        var element = $compile('<div><q-switch value="_value">' +
            '<q-switch-case value="1"><div class="with-value-1">With value 1</div></q-switch-case>' +
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
            '<q-switch-case value=""><div class="default">Default</div></q-switch-case>' +
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

    it("Switches boolean runtime panels.", function() {
        $rootScope._value = false;
        var element = $compile('<div><q-switch value="_value">' +
            '<q-switch-case value="false"><div class="with-value-false">With value false</div></q-switch-case>' +
            '<q-switch-case value="true"><div class="with-value-true">With value true</div></q-switch-case>' +
            '<q-switch-case value=""><div class="default">Default</div></q-switch-case>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-false");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-true");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        $rootScope._value = true;
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-false");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-true");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        $rootScope._value = 3;
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-false");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-true");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(1);
    });

    it("Can be marked with ng-controller directive.", function(){
        var element = $compile('<div><q-switch value="value" ng-controller="TestController">' +
            '<q-switch-case value="1"><div class="with-value-1">With value 1</div></q-switch-case>' +
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
            '<q-switch-case><div class="default">Default</div></q-switch-case>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-1");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);
    });

    it("Can be switched from applied controller.", function(){
        var element = $compile('<div><q-switch value="value" ng-controller="TestController">' +
            '<q-switch-case value="1"><div class="with-value-1">With value 1</div></q-switch-case>' +
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
            '<q-switch-case><div class="default">Default</div></q-switch-case>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-1");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        $rootScope.$broadcast("changeValue", 2);
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-1");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);
    });

    it("Panel element names are values.", function() {
        var element = $compile('<div><q-switch value="\'one\'">' +
            '<two><div class="with-value-2">With value 2</div></two>' +
            '<two><div class="with-value-2-a">With value 2a</div></two>' +
            '<default><div class="default-1">Default 1</div></default>' +
            '<default><div class="default-2">Default 2</div></default>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".default-1");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".default-2");
        expect(innerPanel.length).toEqual(1);

        expect(element.text()).toEqual("Default 1Default 2");
    });

    it("Switches runtime panels with values as element names.", function() {
        $rootScope._value = "one";
        var element = $compile('<div><q-switch value="_value">' +
            '<one><div class="with-value-1">With value 1</div></one>' +
            '<two><div class="with-value-2">With value 2</div></two>' +
            '<default><div class="default">Default</div></default>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-1");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        $rootScope._value = "two";
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

    it("Switches runtime panels with boolean values as element names.", function() {
        $rootScope._value = false;
        var element = $compile('<div><q-switch value="_value">' +
            '<false><div class="with-value-false">With value false</div></false>' +
            '<true><div class="with-value-true">With value truw</div></true>' +
            '<default><div class="default">Default</div></default>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-false");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".with-value-true");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        $rootScope._value = true;
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-false");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-true");
        expect(innerPanel.length).toEqual(1);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(0);

        $rootScope._value = 3;
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-false");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".with-value-true");
        expect(innerPanel.length).toEqual(0);

        var innerPanel = element.find(".default");
        expect(innerPanel.length).toEqual(1);
    });

    it("Has declared default value.", function() {
        var element = $compile('<div><q-switch value="\'two\'" default="two">' +
            '<two><div class="with-value-2">With value 2</div></two>' +
            '<two><div class="with-value-2-a">With value 2a</div></two>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(1);
        var innerPanel = element.find(".with-value-2-a");
        expect(innerPanel.length).toEqual(1);

        expect(element.text()).toEqual("With value 2With value 2a");
    });

    it("Has declared default value and a default panel.", function() {
        var element = $compile('<div><q-switch value="\'two\'" default="two">' +
            '<two><div class="with-value-2">With value 2</div></two>' +
            '<one><div class="with-value-1">With value 1</div></one>' +
            '<default><div class="with-value-2-a">With value 2a</div></default>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(1);
        var innerPanel = element.find(".with-value-2-a");
        expect(innerPanel.length).toEqual(1);

        expect(element.text()).toEqual("With value 2With value 2a");
    });

    it("Has declared default value passed not default value.", function() {
        var element = $compile('<div><q-switch value="\'one\'" default="two">' +
            '<two><div class="with-value-2">With value 2</div></two>' +
            '<one><div class="with-value-1">With value 1</div></one>' +
            '<default><div class="with-value-2-a">With value 2a</div></default>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(0);
        var innerPanel = element.find(".with-value-2-a");
        expect(innerPanel.length).toEqual(0);
        var innerPanel = element.find(".with-value-1");
        expect(innerPanel.length).toEqual(1);

        expect(element.text()).toEqual("With value 1");
    });

    it("Has declared default value passed not listed value.", function() {
        var element = $compile('<div><q-switch value="\'x\'" default="two">' +
            '<two><div class="with-value-2">With value 2</div></two>' +
            '<one><div class="with-value-1">With value 1</div></one>' +
            '<default><div class="with-value-2-a">With value 2a</div></default>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".with-value-2");
        expect(innerPanel.length).toEqual(1);
        var innerPanel = element.find(".with-value-2-a");
        expect(innerPanel.length).toEqual(1);
        var innerPanel = element.find(".with-value-1");
        expect(innerPanel.length).toEqual(0);

        expect(element.text()).toEqual("With value 2With value 2a");
    });

    it("Can use angular binding expression inside.", function() {
        $rootScope.name = "Vasya";

        var element = $compile('<div><q-switch value="\'one\'">' +
            '<one><div class="with-value-1">With value {{name}}</div></one>' +
            '<default><div class="with-value-2-a">With value {{name}}</div></default>' +
            '</q-switch></div>')($rootScope);
        $rootScope.$digest();

        expect(element.text()).toEqual("With value Vasya");
    });

    it("Can be as attribute.", function() {
       var element = $compile('<div><div q-switch="1">' +
               '<q-switch-case value="1"><div class="with-value-1">With value 1</div></q-switch-case>' +
               '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
               '<q-switch-case value="default"><div class="default">Default</div></q-switch-case>' +
           '</div></div>')($rootScope);
        $rootScope.$digest();

        var panel = element.find(".with-value-1");
        expect(panel.length).toEqual(1);

        var panel = element.find(".with-value-2");
        expect(panel.length).toEqual(0);

        var panel = element.find(".default");
        expect(panel.length).toEqual(0);
    });

    it("Can be as attribute and runtime switchable.", function() {
        $rootScope.value = 1;
        var element = $compile('<div><div q-switch="value" >' +
               '<q-switch-case value="1"><div class="with-value-1">With value 1</div></q-switch-case>' +
               '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
               '<q-switch-case value="default"><div class="default">Default</div></q-switch-case>' +
           '</div></div>')($rootScope);
        $rootScope.$digest();

        var panel = element.find(".with-value-1");
        expect(panel.length).toEqual(1);

        var panel = element.find(".with-value-2");
        expect(panel.length).toEqual(0);

        var panel = element.find(".default");
        expect(panel.length).toEqual(0);

        $rootScope.value = 2;
        $rootScope.$digest();

        var panel = element.find(".with-value-1");
        expect(panel.length).toEqual(0);

        var panel = element.find(".with-value-2");
        expect(panel.length).toEqual(1);

        var panel = element.find(".default");
        expect(panel.length).toEqual(0);

        $rootScope.value = 3;
        $rootScope.$digest();

        var panel = element.find(".with-value-1");
        expect(panel.length).toEqual(0);

        var panel = element.find(".with-value-2");
        expect(panel.length).toEqual(0);

        var panel = element.find(".default");
        expect(panel.length).toEqual(1);
    });

    it("Every panel has scope.", function() {
        $rootScope.value = 1;
        var element = $compile('<div><div q-switch="value" >' +
            '<q-switch-case value="1"><div class="with-value-1">With value 1</div></q-switch-case>' +
            '<q-switch-case value="2"><div class="with-value-2">With value 2</div></q-switch-case>' +
            '<q-switch-case value="default"><div class="default">Default</div></q-switch-case>' +
            '</div></div>')($rootScope);
        $rootScope.$digest();

        var scope = element.find(".with-value-1").scope();
        expect(scope).toBeDefined();
        expect(scope).not.toBeNull();
        //expect(scope.$parent).toBeDefined();
        //expect(scope.$parent).not.toBeNull();

        $rootScope.value = 2;
        $rootScope.$digest();

        var scope = element.find(".with-value-2").scope();
        expect(scope).toBeDefined();
        expect(scope).not.toBeNull();
        //expect(scope.$parent).toBeDefined();
        //expect(scope.$parent).not.toBeNull();

        $rootScope.value = 3;
        $rootScope.$digest();

        var scope = element.find(".default").scope();
        expect(scope).toBeDefined();
        expect(scope).not.toBeNull();
        //expect(scope.$parent).toBeDefined();
        //expect(scope.$parent).not.toBeNull();
    });

    it("Updates inner panel elements", function() {
        $rootScope.value = 1;
        $rootScope.name1 = "A1";
        $rootScope.name2 = "A2";
        var element = $compile('<div><div q-switch="value" >' +
            '<q-switch-case value="1"><div class="with-value-1">With value {{name1}}</div></q-switch-case>' +
            '<q-switch-case value="2"><div class="with-value-2">With value {{name2}}</div></q-switch-case>' +
            '<q-switch-case value="default"><div class="default">Default</div></q-switch-case>' +
            '</div></div>')($rootScope);
        $rootScope.$digest();

        expect(element.find(".with-value-1").text()).toEqual("With value A1");

        $rootScope.name1 = "B1";
        $rootScope.$digest();
        expect(element.find(".with-value-1").text()).toEqual("With value B1");

        $rootScope.name2 = "B2";
        $rootScope.$digest();
        expect(element.find(".with-value-1").text()).toEqual("With value B1");

        $rootScope.value = 2;
        $rootScope.$digest();
        expect(element.find(".with-value-2").text()).toEqual("With value B2");
    });
});