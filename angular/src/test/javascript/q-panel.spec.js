describe("Test q-panel panel.", function() {
    var $compile, $rootScope;
    var testModule;

    beforeEach(function() {
        testModule = angular.module("TestQPanelModule", []);
        testModule.controller("TestController", function($scope){
            $scope.value = true;

            $scope.$on("changeValue", function(event, newValue) {
                $scope.value = newValue;
            });
        });

        module("qitcommonsModule");
        module("TestQPanelModule");

        inject(function(_$compile_, _$rootScope_) {
            $compile = _$compile_;
            $rootScope = _$rootScope_;
        });
    });

    it("Adds content of panel to document.", function() {
        var element = $compile('<div><q-panel><div class="inner-panel">Hello</div></q-panel></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".inner-panel");
        var qPanel = element.find("q-panel");

        expect(innerPanel.length).toEqual(1);
        expect(qPanel.length).toEqual(0);
    });

    it("Remove panel from DOM on hidding.", function() {
        var element = $compile('<div><q-panel show="false"><div class="inner-panel">Hello</div></q-panel></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".inner-panel");

        expect(innerPanel.length).toEqual(0);
    });

    it("Do not has panel element", function() {
        var element = $compile('<div><div class="outer-panel"><q-panel><div class="inner-panel">Hello</div></q-panel></div></div>')($rootScope);
        $rootScope.$digest();

        var panels = element.find(".outer-panel > .inner-panel");

        expect(panels.length).toEqual(1);
    });

    it("Runtime hides panel.", function() {
        $rootScope._show = true;
        var element = $compile('<div><q-panel show="_show"><div class="inner-panel">Hello</div></q-panel></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".inner-panel");
        expect(innerPanel.length).toEqual(1);

        $rootScope._show = false;
        $rootScope.$digest();
        var innerPanel = element.find(".inner-panel");
        expect(innerPanel.length).toEqual(0);
    });

    it("Can be marked with ng-controller directive.", function(){
        var element = $compile('<div><q-panel show="value" ng-controller="TestController"><div class="inner-panel">Hello</div></q-panel></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".inner-panel");
        expect(innerPanel.length).toEqual(1);
    });

    it("Can be hidden from applied controller.", function(){
        var element = $compile('<div><q-panel show="value" ng-controller="TestController"><div class="inner-panel">Hello</div></q-panel></div>')($rootScope);
        $rootScope.$digest();

        var innerPanel = element.find(".inner-panel");
        expect(innerPanel.length).toEqual(1);

        $rootScope.$broadcast("changeValue", false);
        $rootScope.$digest();

        var innerPanel = element.find(".inner-panel");
        expect(innerPanel.length).toEqual(0);
    });

});