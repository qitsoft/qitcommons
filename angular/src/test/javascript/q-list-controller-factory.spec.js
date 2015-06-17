describe("Test qListController service.", function () {
    const QUERY = "qwerty";

    beforeEach(module('qitcommonsModule'));

    var $location, $timeout, $qListController, $q, $rootScope, $scope;
    var $config, $service, $successCallback, $errorCallback, $serviceResult;
    var $locationSearch;
    beforeEach(inject(function (_$location_, _$timeout_, _$qListController_, _$q_, _$rootScope_) {
        $location = _$location_;
        $timeout = _$timeout_;
        $qListController = _$qListController_;
        $q = _$q_;
        $rootScope = _$rootScope_;
    }));

    beforeEach(function () {
        $scope = $rootScope.$new();
        $serviceResult = {"service": "test"};
        $service = jasmine.createSpy("$service").and.returnValue($serviceResult);
        $successCallback = jasmine.createSpy("$successCallback");
        $errorCallback = jasmine.createSpy("$errorCallback");
        $config = {
            service: $service,
            success: $successCallback,
            error: $errorCallback
        }
        $locationSearch = {};
        spyOn($location, "search").and.returnValue($locationSearch);
    });

    it("Requests data from service for page 1 with size 10.", function () {
        $qListController($scope, $config);

        expect($service).toHaveBeenCalled();
        var params = $service.calls.argsFor(0)[0];
        expect(params).not.toBeNull();
        expect(params.query).toBeUndefined();
        expect(params.page).toEqual(1);
        expect(params.pageSize).toEqual(10);
    });

    it("Passes success and error callbacks.", function () {
        $qListController($scope, $config);

        expect($service).toHaveBeenCalled();
        expect($service.calls.argsFor(0)[1]).toBe($successCallback);
        expect($service.calls.argsFor(0)[2]).toBe($errorCallback);
    });

    it("Requests the page from config.", function () {
        $config.page = 3;

        $qListController($scope, $config);

        expect($service.calls.argsFor(0)[0].page).toEqual(3);
    });

    it("Requests the page from location 'p' parameter.", function () {
        $locationSearch.p = 3;

        $qListController($scope, $config);

        expect($service.calls.argsFor(0)[0].page).toEqual(3);
    });

    it("Requests the page from location 'p' parameter if is number.", function () {
        $locationSearch.p = "dsds";

        $qListController($scope, $config);

        expect($service.calls.argsFor(0)[0].page).toEqual(1);
    });

    it("The config page parameter takes precedence.", function () {
        $config.page = 2;
        $locationSearch.p = 3;

        $qListController($scope, $config);

        expect($service.calls.argsFor(0)[0].page).toEqual(2);
    });

    it("The config page parameter takes precedence if is number.", function () {
        $config.page = "sas";
        $locationSearch.p = 3;

        $qListController($scope, $config);

        expect($service.calls.argsFor(0)[0].page).toEqual(3);
    });

    it("Returns result from service and sets to scope.", function () {
        $qListController($scope, $config);

        expect($scope.list).toBe($serviceResult);
    });

    it("Uses pageSize config property.", function () {
        $config.pageSize = 20;

        $qListController($scope, $config);

        expect($service.calls.argsFor(0)[0].pageSize).toEqual(20);
    });

    it("Sets scope page and pageSize properties.", function () {
        $config.pageSize = 20;
        $config.page = 3;

        $qListController($scope, $config);

        expect($scope.pageSize).toEqual(20);
        expect($scope.page).toEqual(3);
    });

    it("Sets query value from location.", function () {
        $locationSearch.q = QUERY;

        $qListController($scope, $config);

        expect($service.calls.argsFor(0)[0].query).toEqual(QUERY);
        expect($scope.query).toEqual(QUERY);
    });

    it("Focus the filter input by jquery element.", function () {
        var inputFilter = $("<input>");
        $config.filter = inputFilter;
        spyOn(inputFilter, "focus");

        $qListController($scope, $config);

        expect(inputFilter.focus).toHaveBeenCalled();
    });

    it("Focus the filter input by DOM element.", function () {
        var inputFilter = $("<input>").get(0);
        $config.filter = inputFilter;
        spyOn(inputFilter, "focus");

        $qListController($scope, $config);

        expect(inputFilter.focus).toHaveBeenCalled();
    });

    it("Focus the filter input by element selector.", function () {
        var inputFilter = $("<input>");
        inputFilter.attr("id", "inputFilter");
        $(document.body).append(inputFilter);
        $config.filter = "#inputFilter";
        spyOn($.fn, "focus");
        spyOn(window, "$").and.callThrough();

        $qListController($scope, $config);

        expect(window.$).toHaveBeenCalledWith("#inputFilter");
        expect($.fn.focus).toHaveBeenCalled();

        $("#inputFilter").remove();
    });

    it("Typing with unsing delay from config.", function() {
        $config.typingTimeout = 2000;
        $qListController($scope, $config);
        $location.search.calls.reset();
        spyOn($scope, "filter");

        $scope.typingFilter(QUERY);

        expect($scope.filter).not.toHaveBeenCalled();

        $timeout.flush(1999);
        expect($scope.filter).not.toHaveBeenCalled();

        $timeout.flush(1);
        expect($scope.filter).toHaveBeenCalledWith(QUERY);
    });

    describe("Test filtering.", function () {

        beforeEach(function () {
            $qListController($scope, $config);

            expect($scope.filter).toBeDefined();
            $location.search.calls.reset();
        });

        it("Filter items by passed parameter.", function () {
            $scope.filter(QUERY);

            expect($location.search).toHaveBeenCalledWith("p", undefined);
            expect($location.search).toHaveBeenCalledWith("q", QUERY);
            expect($scope.query).toEqual(QUERY);
        });

        it("Filter items by passed parameter if it is string.", function () {
            $scope.filter(["dsds"]);

            expect($location.search).not.toHaveBeenCalled();
            expect($location.search).not.toHaveBeenCalled();
        });

        it("Reset filter by passed parameter null.", function () {
            $scope.filter(null);

            expect($location.search).toHaveBeenCalledWith("p", undefined);
            expect($location.search).toHaveBeenCalledWith("q", undefined);
            expect($scope.query).toEqual(null);
        });

        it("Filter searched by scope variable.", function () {
            $scope.query = QUERY;
            $scope.filter();

            expect($location.search).toHaveBeenCalledWith("p", undefined);
            expect($location.search).toHaveBeenCalledWith("q", QUERY);
        });

        it("Filter searched by passed parameter over scope variable.", function () {
            $scope.query = "not-processed";
            $scope.filter(QUERY);

            expect($location.search).toHaveBeenCalledWith("p", undefined);
            expect($location.search).toHaveBeenCalledWith("q", QUERY);
            expect($scope.query).toEqual(QUERY);
        });

        it("Filter searched by scope variable if it is string.", function () {
            $scope.query = [QUERY];
            $scope.filter();

            expect($location.search).not.toHaveBeenCalled();
            expect($location.search).not.toHaveBeenCalled();
        });

        it("Reset filter by scope variable if it null.", function () {
            $scope.query = null;
            $scope.filter();

            expect($location.search).toHaveBeenCalledWith("p", undefined);
            expect($location.search).toHaveBeenCalledWith("q", undefined);
            expect($scope.query).toEqual(null);
        });

        it("Clears filter.", function () {
            $scope.query = QUERY;

            $scope.clearFilter();

            expect($location.search).toHaveBeenCalledWith("q", undefined);
            expect($scope.query).toBeNull();
        });

        it("Stops typing timeout on manual filtering.", function() {
            spyOn($scope, "filter").and.callThrough();

            $scope.typingFilter();
            $timeout.flush(500);
            $scope.filter(QUERY);
            $scope.filter.calls.reset();

            $timeout.flush(5000);
            expect($scope.filter).not.toHaveBeenCalled();
        });

        describe("Typing filter.", function() {

            beforeEach(function() {
                spyOn($scope, "filter");
            });

            it("With 1 second delay and filter by passed parameter.", function() {
                $scope.typingFilter(QUERY);

                testTimoutFiltering(QUERY);
            });

            it("With 1 second delay and filter by scope variable.", function() {
                $scope.typingFilter();

                testTimoutFiltering();
            });

            it("Stops timeout on clearing filter.", function() {
                $scope.typingFilter();
                $timeout.flush(500);
                $scope.clearFilter();

                $timeout.flush(5000);
                expect($scope.filter).not.toHaveBeenCalled();
            });

            it("Stops timeout on consecutive filer typing.", function() {
                $scope.typingFilter();
                $timeout.flush(500);
                $scope.typingFilter();

                testTimoutFiltering();
            });

            function testTimoutFiltering(arg) {
                expect($scope.filter).not.toHaveBeenCalled();

                $timeout.flush(999);
                expect($scope.filter).not.toHaveBeenCalled();

                $timeout.flush(1);
                expect($scope.filter).toHaveBeenCalledWith(arg);
            }
        });
    });
});