describe("Test q-error-message directive.", function () {
    beforeEach(module('qitcommonsModule'));

    var $compile, $rootScope;
    beforeEach(inject(function (_$compile_, _$rootScope_) {
        $compile = _$compile_;
        $rootScope = _$rootScope_;

        spyOn($.fn, "fadeIn");
        spyOn($.fn, "fadeOut");
    }));

    it("Adds span.q-error-message.help-block element.", function () {
        var element = $compile('<form name="fName"><input name="firstName" ng-model="fname"/><q-error-message for="firstName" error="required">The error message for first name.</q-error-message></form>')($rootScope);
        $rootScope.$digest();

        expect(element.find("span.q-error-message.help-block").length).toEqual(1);
    });

    it("Replaces angular directive.", function () {
        var element = $compile('<form name="fName"><input name="firstName" ng-model="fname" /><q-error-message for="firstName" error="required">The error message for first name.</q-error-message></form>')($rootScope);
        $rootScope.$digest();

        expect(element.find("q-error-message").length).toEqual(0);
    });

    it("Includes message text.", function () {
        var element = $compile('<form name="fName"><input name="firstName" ng-model="fname" /><q-error-message for="firstName" error="required">The error message for first name.</q-error-message></form>')($rootScope);
        $rootScope.$digest();

        expect(element.find("span.help-block").text()).toEqual("The error message for first name.");
    });

    it("Is hidden by default.", function () {
        var element = $compile('<form name="fName"><input name="firstName" ng-model="fname" /><q-error-message for="firstName" error="required"/></form>')($rootScope);
        $rootScope.$digest();

        expect(element.find("span.help-block:hidden").length).toEqual(1);
    });

    it("It remains hidden for invalid input for other error.", function () {
        var element = $compile('<form name="fName"><input name="firstName" ng-model="fname" required/><q-error-message for="firstName" error="pattern"/></form>')($rootScope);
        $rootScope.$digest();

        expect(element.find("span.help-block:hidden").length).toEqual(1);
    });

    it("Fails when no 'for' attribute specified.", function () {
        expect(function() {
            $compile('<form name="fName"><input name="firstName" ng-model="name"/><q-error-message>The error message for first name.</q-error-message></form>')($rootScope);
        }).toThrowError("The qErrorMessage directive in form 'fName' with text 'The error message for first name.' should have 'for' attribute specified.");
    });

    it("Fails when not in form.", function () {
        expect(function () {
            $compile('<input name="name" ng-model="name"/><q-error-message>The error message for first name.</q-error-message>')($rootScope);
        }).toThrowError(/\[\$compile:ctreq\][\s\S]*?\$compile\/ctreq\?p0=form&p1=qErrorMessage/);
    });

    it("Fails form without name.", function () {
        expect(function() {
            $compile('<form><input name="firstName" ng-model="name" required/><q-error-message for="firstName" error="required">The error message for first name.</q-error-message></form>')($rootScope);
        }).toThrowError("The qErrorMessage directive with text 'The error message for first name.' should be placed in form with name.");
    });

    it("Fails when no 'error' attribute specified.", function () {
        expect(function() {
            $compile('<form name="fName"><input name="firstName" ng-model="name"/><q-error-message for="firstName">The error message for first name.</q-error-message></form>')($rootScope);
        }).toThrowError("The qErrorMessage directive in form 'fName' with text 'The error message for first name.' should have 'error' attribute specified.");
    });

    describe("Listens for validity status.", function () {
        var $input, $message;
        beforeEach(function() {
            var form = $compile('<form name="fName"><input name="firstName" ng-model="fname" required ng-pattern="/dd/"/><q-error-message for="firstName" error="required"/></form>')($rootScope);
            $rootScope.$digest();
            $input = form.find("input");
            $message = form.find("span.help-block");
        });

        it("Fades in for invalid input for specific error.", function() {
            expect($.fn.fadeIn).toHaveBeenCalled();
        });

        it("Fades out for invalid input which becomes valid.", function() {
            expect($.fn.fadeIn).toHaveBeenCalled();
            $rootScope.fName.firstName.$setViewValue("dd");
            $rootScope.$digest();
            expect($.fn.fadeOut).toHaveBeenCalled();
        });

        it("Initially sets as invalid and pristine.", function() {
            expect(classes()).toContain("ng-pristine");
            expect(classes()).toContain("ng-invalid");
        });

        it("Becomes hidden on value change.", function() {
            $rootScope.fName.firstName.$setViewValue("dd");
            $rootScope.$digest();

            expect($.fn.fadeOut).toHaveBeenCalled();
        });

        it("Becomes invalid and dirty on value change.", function() {
            $rootScope.fName.firstName.$setViewValue("dd");
            $rootScope.$digest();
            $rootScope.fName.firstName.$setViewValue("");
            $rootScope.$digest();

            expect(classes()).toContain("ng-dirty");
            expect(classes()).toContain("ng-invalid");
        });

        it("Becomes hidden on value change even if input is still invalid.", function() {
            $rootScope.fName.firstName.$setViewValue("dx2232323");
            $rootScope.$digest();

            expect($rootScope.fName.firstName.$error.pattern).toBe(true);
            expect($.fn.fadeOut).toHaveBeenCalled();
        });

        it("Becomes dirty on method call.", function() {
            $rootScope.fName.firstName.$setDirty();
            $rootScope.$digest();

            expect(classes()).toContain("ng-dirty");
        });

        it("Becomes pristine on method call.", function() {
            $rootScope.fName.firstName.$setViewValue("dd");
            $rootScope.$digest();
            expect(classes()).toContain("ng-dirty");

            $rootScope.fName.firstName.$setPristine();
            $rootScope.$digest();

            expect(classes()).toContain("ng-pristine");
        });

        function classes() {
            return $message.attr("class").split(/\s+/);
        }
    });
});