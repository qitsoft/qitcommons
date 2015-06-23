describe("Testing q-pager directive.", function () {
    beforeEach(module('qitcommonsModule'));

    var $compile, $rootScope, $location, $search;
    beforeEach(inject(function (_$compile_, _$rootScope_, _$location_) {
        $compile = _$compile_;
        $rootScope = _$rootScope_;
        $location = _$location_;
    }));

    beforeEach(function () {
        $search = {};
        spyOn($location, "search").and.returnValue($search);
    });

    var $element;

    it("Places bootstrap paging component.", function () {
        $element = $compile('<div><q-pager page="1" page-size="10" items="10" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination").length).toEqual(1);
    });

    it("Adds page numbers by items.", function () {
        $element = $compile('<div><q-pager page="1" page-size="10" items="30" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>:contains('1')").length).toEqual(1);
        expect($element.find("ul.q-pager.pagination>:contains('2')").length).toEqual(1);
        expect($element.find("ul.q-pager.pagination>:contains('3')").length).toEqual(1);
    });

    it("Adds page numbers by pages.", function () {
        $element = $compile('<div><q-pager page="1" total="3" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>:contains('1')").length).toEqual(1);
        expect($element.find("ul.q-pager.pagination>:contains('2')").length).toEqual(1);
        expect($element.find("ul.q-pager.pagination>:contains('3')").length).toEqual(1);
    });

    it("Adds prev and next.", function () {
        $element = $compile('<div><q-pager page="1" total="3" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>:contains('«')").length).toEqual(1);
        expect($element.find("ul.q-pager.pagination>:contains('»')").length).toEqual(1);
    });

    it("Current page is active.", function () {
        $element = $compile('<div><q-pager page="2" total="3" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>li.active:contains('2')").length).toEqual(1);
    });

    it("Not current pages are not active.", function () {
        $element = $compile('<div><q-pager page="2" total="3" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>li:not(.active:contains('2'))").length).toEqual(4);
    });

    it("Ellipsis are not clickable.", function () {
        $element = $compile('<div><q-pager page="10" total="20" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('…')").length).toEqual(2);
    });

    it("Prev is not clickable on first page.", function () {
        $element = $compile('<div><q-pager page="1" total="20" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('«')").length).toEqual(1);
    });

    it("Prev is clickable on second page.", function () {
        $element = $compile('<div><q-pager page="2" total="20" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>li>a:contains('«')").length).toEqual(1);
    });

    it("Prev is clickable on last page.", function () {
        $element = $compile('<div><q-pager page="20" total="20" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>li>a:contains('«')").length).toEqual(1);
    });

    it("Next is not clickable on last page.", function () {
        $element = $compile('<div><q-pager page="20" total="20" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('»')").length).toEqual(1);
    });

    it("Next is clickable on the page before the last.", function () {
        $element = $compile('<div><q-pager page="19" total="20" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>li>a:contains('»')").length).toEqual(1);
    });

    it("Next is clickable on the first page.", function () {
        $element = $compile('<div><q-pager page="1" total="20" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>li>a:contains('»')").length).toEqual(1);
    });

    it("Moving page to the last when it exceeds total pages.", function () {
        $element = $compile('<div><q-pager page="40" total="20" /></div>')($rootScope);
        $rootScope.$digest();

        expect($location.search).toHaveBeenCalledWith("p", 20);
    });

    it("Moving page to the first when it less then 1.", function () {
        $element = $compile('<div><q-pager page="0" total="20" /></div>')($rootScope);
        $rootScope.$digest();

        expect($location.search).toHaveBeenCalledWith("p", null);
    });

    it("Is hidden when total is 0.", function () {
        $element = $compile('<div><q-pager page="1" total="0" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination:hidden").length).toEqual(1);
    });

    it("Is hidden when total is NaN.", function () {
        $element = $compile('<div><q-pager page="1" total="dsds" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination:hidden").length).toEqual(1);
    });

    it("Is hidden when total is undefined.", function () {
        $element = $compile('<div><q-pager page="1" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination:hidden").length).toEqual(1);
    });

    it("Is hidden when items is 0.", function () {
        $element = $compile('<div><q-pager page="1" page-size="10" items="0" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination:hidden").length).toEqual(1);
    });

    it("Is hidden when items is NaN.", function () {
        $element = $compile('<div><q-pager page="1" page-size="10" items="dsds" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination:hidden").length).toEqual(1);
    });

    it("Is hidden when items is undefined.", function () {
        $element = $compile('<div><q-pager page="1" page-size="10" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination:hidden").length).toEqual(1);
    });

    it("Throws exception if page-size is undefined and items are declared.", function () {
        expect(function () {
            $compile('<div><q-pager page="1" items="10" /></div>')($rootScope);
        }).toThrowError("Page size should be defined and should be a number in component qPager.");
    });

    it("Throws exception if page-size is not number and items are declared.", function () {
        expect(function () {
            $compile('<div><q-pager page="1" items="10" page-size="sdfsdf" /></div>')($rootScope);
        }).toThrowError("Page size should be defined and should be a number in component qPager.");
    });

    it("Throws exception if page-size is 0 and items are declared.", function () {
        expect(function () {
            $compile('<div><q-pager page="1" items="10" page-size="0" /></div>')($rootScope);
        }).toThrowError("Page size should be greater than 0 in component qPager.");
    });

    it("Gets the current page from location search p value.", function() {
        $search.p = 2;
        $element = $compile('<div><q-pager total="3" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>li.active:contains('2')").length).toEqual(1);
    });

    it("The default page value is 1.", function() {
        $element = $compile('<div><q-pager total="3" /></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ul.q-pager.pagination>li.active:contains('1')").length).toEqual(1);
    });

    describe("Split pages to fit only 10.", function () {
        it("Shows 8 pages right to current page, ellipsis and the last.", function () {
            $element = $compile('<div><q-pager page="1" total="15" /></div>')($rootScope);
            $rootScope.$digest();

            checkParts(1, 9, 15);
        });

        it("Shows 8 pages left to current page, ellipsis and the first.", function () {
            $element = $compile('<div><q-pager page="20" total="20" /></div>')($rootScope);
            $rootScope.$digest();

            checkParts(12, 20);
        });

        it("Shows 10 pages if total are 10", function () {
            $element = $compile('<div><q-pager page="1" total="10" /></div>')($rootScope);
            $rootScope.$digest();

            var pages = $.makeArray($element.find("ul.q-pager.pagination>*").map(function (i, e) {
                return e.innerText;
            }));
            pages = pages.slice(1, pages.length - 1);

            for (var i = 1; i <= 10; i++) {
                expect(pages.shift()).toEqual(i.toString());
            }
            expect(pages.length).toEqual(0);
        });

        it("Shows in the middle by 4 on each side.", function () {
            $element = $compile('<div><q-pager page="10" total="20" /></div>')($rootScope);
            $rootScope.$digest();

            checkParts(6, 14, 20);
        });

        it("Shows 5 pages right to current page, ellipsis and the last when current page is 3 of 15.", function () {
            $element = $compile('<div><q-pager page="3" total="15" /></div>')($rootScope);
            $rootScope.$digest();

            checkParts(1, 9, 15);
        });

        it("Shows 5 pages left to current page, ellipsis and the first when current page is 17 of 20.", function () {
            $element = $compile('<div><q-pager page="17" total="20" /></div>')($rootScope);
            $rootScope.$digest();

            checkParts(12, 20);
        });

        it("Shows all pages if total are less than 10", function () {
            $element = $compile('<div><q-pager page="1" total="7" /></div>')($rootScope);
            $rootScope.$digest();

            var pages = $.makeArray($element.find("ul.q-pager.pagination>*").map(function (i, e) {
                return e.innerText;
            }));
            pages = pages.slice(1, pages.length - 1);

            for (var i = 1; i <= 7; i++) {
                expect(pages.shift()).toEqual(i.toString());
            }
            expect(pages.length).toEqual(0);
        });

        it("Shows all pages if calculated total are less than 10", function () {
            $element = $compile('<div><q-pager page="1" page-size="10" items="70" /></div>')($rootScope);
            $rootScope.$digest();

            var pages = $.makeArray($element.find("ul.q-pager.pagination>*").map(function (i, e) {
                return e.innerText;
            }));
            pages = pages.slice(1, pages.length - 1);

            for (var i = 1; i <= 7; i++) {
                expect(pages.shift()).toEqual(i.toString());
            }
            expect(pages.length).toEqual(0);
        });
    });

    describe("Listens for property changes with initial total passed.", function () {
        beforeEach(function () {
            $rootScope._page = 10;
            $rootScope._total = 20;
            $element = $compile('<div><q-pager page="{{_page}}" total="{{_total}}" /></div>')($rootScope);
            $rootScope.$digest();
        });

        it("Check visible pages when page number changed.", function () {
            $rootScope._page = 11;
            $rootScope.$digest();

            checkParts(7, 15, 20);
        });

        it("Check last page when total changed.", function () {
            $rootScope._total = 21;
            $rootScope.$digest();

            checkParts(6, 14, 21);
        });

        it("Check visible pages when total changed.", function () {
            $rootScope._total = 21;
            $rootScope._page = 21;
            $rootScope.$digest();

            checkParts(13, 21);
        });

        it("Check disabling prev on moving to first page.", function () {
            $rootScope._page = 1;
            $rootScope.$digest();

            expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('«')").length).toEqual(1);
        });

        it("Check enabling prev on moving to second page.", function () {
            $rootScope._page = 1;
            $rootScope.$digest();
            expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('«')").length).toEqual(1);

            $rootScope._page = 2;
            $rootScope.$digest();

            expect($element.find("ul.q-pager.pagination>li>a:contains('«')").length).toEqual(1);
        });

        it("Check disabling next on moving to last page.", function () {
            $rootScope._page = 20;
            $rootScope.$digest();

            expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('»')").length).toEqual(1);
        });

        it("Check enabling next on moving to the page before the last.", function () {
            $rootScope._page = 20;
            $rootScope.$digest();
            expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('»')").length).toEqual(1);

            $rootScope._page = 19;
            $rootScope.$digest();

            expect($element.find("ul.q-pager.pagination>li>a:contains('»')").length).toEqual(1);
        });

        it("Check enabling next on extending total.", function () {
            $rootScope._page = 20;
            $rootScope.$digest();
            expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('»')").length).toEqual(1);

            $rootScope._total = 21;
            $rootScope.$digest();

            expect($element.find("ul.q-pager.pagination>li>a:contains('»')").length).toEqual(1);
            expect($element.find("ul.q-pager.pagination>li.active>a:contains('20')").length).toEqual(1);
        });

        it("Check moving the page to the last when total goes down.", function () {
            $rootScope._total = 9;
            $rootScope.$digest();

            expect($location.search).toHaveBeenCalledWith("p", 9);
        });
    });

    describe("Listens for property changes with initial items passed.", function () {
        beforeEach(function () {
            $rootScope._page = 10;
            $rootScope._items = 200;
            $rootScope._pageSize = 10;
            $element = $compile('<div><q-pager page="{{_page}}" items="{{_items}}" page-size="{{_pageSize}}" /></div>')($rootScope);
            $rootScope.$digest();
        });

        it("Check visible pages when page number changed.", function () {
            $rootScope._page = 11;
            $rootScope.$digest();

            checkParts(7, 15, 20);
        });

        it("Check last page when items changed.", function () {
            $rootScope._items = 205;
            $rootScope.$digest();

            checkParts(6, 14, 21);
        });

        it("Check last page when page-size changed.", function () {
            $rootScope._pageSize = 9;
            $rootScope.$digest();

            checkParts(6, 14, 23);
        });

        it("Check visible pages when items changed.", function () {
            $rootScope._items = 205;
            $rootScope._page = 21;
            $rootScope.$digest();

            checkParts(13, 21);
        });

        it("Check disabling prev on moving to first page.", function () {
            $rootScope._page = 1;
            $rootScope.$digest();

            expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('«')").length).toEqual(1);
        });

        it("Check enabling prev on moving to second page.", function () {
            $rootScope._page = 1;
            $rootScope.$digest();
            expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('«')").length).toEqual(1);

            $rootScope._page = 2;
            $rootScope.$digest();

            expect($element.find("ul.q-pager.pagination>li>a:contains('«')").length).toEqual(1);
        });

        it("Check disabling next on moving to last page.", function () {
            $rootScope._page = 20;
            $rootScope.$digest();

            expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('»')").length).toEqual(1);
        });

        it("Check enabling next on moving to the page before the last.", function () {
            $rootScope._page = 20;
            $rootScope.$digest();
            expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('»')").length).toEqual(1);

            $rootScope._page = 19;
            $rootScope.$digest();

            expect($element.find("ul.q-pager.pagination>li>a:contains('»')").length).toEqual(1);
        });

        it("Check enabling next on extending items.", function () {
            $rootScope._page = 20;
            $rootScope.$digest();
            expect($element.find("ul.q-pager.pagination>li.disabled>span:contains('»')").length).toEqual(1);

            $rootScope._items = 207;
            $rootScope.$digest();

            expect($element.find("ul.q-pager.pagination>li>a:contains('»')").length).toEqual(1);
            expect($element.find("ul.q-pager.pagination>li.active>a:contains('20')").length).toEqual(1);
        });

        it("Check moving the page to the last when items goes down.", function () {
            $rootScope._items = 90;
            $rootScope.$digest();

            expect($location.search).toHaveBeenCalledWith("p", 9);
        });
    });

    describe("Check default actions.", function () {
        beforeEach(function () {
            $element = $compile('<div><q-pager page="5" total="10" /></div>')($rootScope);
            $rootScope.$digest();
        });

        it("Changes location search's p value.", function () {
            $element.find("li>a:contains('3')").click();

            expect($location.search).toHaveBeenCalledWith("p", 3);
        });

        it("Changes location resets search's value on moving to first page.", function () {
            $element.find("li>a:contains('1')").click();

            expect($location.search).toHaveBeenCalledWith("p", null);
        });

        it("Changes location on prev click.", function () {
            $element.find("li>a:contains('«')").click();

            expect($location.search).toHaveBeenCalledWith("p", 4);
        });

        it("Changes location on next click.", function () {
            $element.find("li>a:contains('»')").click();

            expect($location.search).toHaveBeenCalledWith("p", 6);
        });
    });

    describe("Check custom actions.", function() {
        beforeEach(function () {
            $rootScope.changePage = jasmine.createSpy("changePage");
            $element = $compile('<div><q-pager page="5" total="10" change="changePage($page, $oldPage)" /></div>')($rootScope);
            $rootScope.$digest();
        });

        it("Changes location search's p value.", function () {
            $element.find("li>a:contains('3')").click();

            expect($rootScope.changePage).toHaveBeenCalledWith(3, 5);
        });

        it("Changes location resets search's value on moving to first page.", function () {
            $element.find("li>a:contains('1')").click();

            expect($rootScope.changePage).toHaveBeenCalledWith(1, 5);
        });

        it("Changes location on prev click.", function () {
            $element.find("li>a:contains('«')").click();

            expect($rootScope.changePage).toHaveBeenCalledWith(4, 5);
        });

        it("Changes location on next click.", function () {
            $element.find("li>a:contains('»')").click();

            expect($rootScope.changePage).toHaveBeenCalledWith(6, 5);
        });

        it("Check moving the page to the last when items goes down.", function () {
            $rootScope._items = 100;
            $element = $compile('<div><q-pager page="5" page-size="10" items="{{_items}}" change="changePage($page, $oldPage)" /></div>')($rootScope);
            $rootScope.$digest();

            $rootScope._items = 40;
            $rootScope.$digest();

            expect($rootScope.changePage).toHaveBeenCalledWith(4, 5);
        });

        it("Check moving the page to the last when total goes down.", function () {
            $rootScope._total = 10;
            $element = $compile('<div><q-pager page="5" total="{{_total}}" change="changePage($page, $oldPage)" /></div>')($rootScope);
            $rootScope.$digest();

            $rootScope._total = 4;
            $rootScope.$digest();

            expect($rootScope.changePage).toHaveBeenCalledWith(4, 5);
        });

        it("Moving page to the last when it exceeds total pages.", function () {
            $element = $compile('<div><q-pager page="40" total="20" change="changePage($page, $oldPage)"/></div>')($rootScope);
            $rootScope.$digest();

            expect($rootScope.changePage).toHaveBeenCalledWith(20, 40);
        });

        it("Moving page to the first when it less then 1.", function () {
            $element = $compile('<div><q-pager page="0" total="20" change="changePage($page, $oldPage)"/></div>')($rootScope);
            $rootScope.$digest();

            expect($rootScope.changePage).toHaveBeenCalledWith(1, 0);
        });
    });

    function checkParts(lower, upper, last) {
        var pages = $.makeArray($element.find("ul.q-pager.pagination>*").map(function (i, e) {
            return e.innerText;
        }));
        pages = pages.slice(1, pages.length - 1);

        if (lower > 1) {
            expect(pages.shift()).toEqual((1).toString());
            expect(pages.shift()).toEqual("…");
        }
        for (var i = lower; i <= upper; i++) {
            expect(pages.shift()).toEqual(i.toString());
        }
        if (last) {
            expect(pages.shift()).toEqual("…");
            expect(pages.shift()).toEqual(last.toString());
        }
        expect(pages.length).toEqual(0);
    }

});