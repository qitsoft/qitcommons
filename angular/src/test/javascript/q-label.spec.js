describe("Testing q-label directive.", function () {
    beforeEach(module('qitcommonsModule'));

    var $compile, $rootScope;
    beforeEach(inject(function (_$compile_, _$rootScope_) {
        $compile = _$compile_;
        $rootScope = _$rootScope_;
    }));

    it("Shows label.", function () {
        var element = $compile('<form><input name="name" ng-model="name"/><q-label for="name">Name</q-label></form>')($rootScope);
        $rootScope.$digest();

        var label = element.find("label");
        expect(label.length).toEqual(1);
        expect(label.attr("for")).toEqual("name");
        expect(label.text()).toEqual("Name");

        var classes = getClasses(label);
        expect(classes).toContain("q-label");
        expect(classes).toContain("control-label");
    });

    it("Set label class names.", function () {
        var element = $compile('<form><input name="name" ng-model="name"/><q-label for="name" class="abra">Name</q-label></form>')($rootScope);
        $rootScope.$digest();

        var classes = getClasses(element.find("label"));
        expect(classes).toContain("abra");
    });

    it("Set label disabled", function () {
        var element = $compile('<form><input name="name" ng-model="name"/><q-label for="name" disabled="true">Name</q-label></form>')($rootScope);
        $rootScope.$digest();

        var classes = getClasses(element.find("label"));
        expect(classes).toContain("disabled");
    });

    it("Set label enabled", function () {
        var element = $compile('<form><input name="name" ng-model="name"/><q-label for="name" disabled="false">Name</q-label></form>')($rootScope);
        $rootScope.$digest();

        var classes = getClasses(element.find("label"));
        expect(classes).not.toContain("disabled");
    });

    it("Set label default enabled", function () {
        var element = $compile('<form><input name="name" ng-model="name"/><q-label for="name">Name</q-label></form>')($rootScope);
        $rootScope.$digest();

        var classes = getClasses(element.find("label"));
        expect(classes).not.toContain("disabled");
    });

    it("Set label enabled for other disabled attribute string values.", function () {
        var element = $compile('<form><input name="name" ng-model="name"/><q-label for="name" disabled="sasas">Name</q-label></form>')($rootScope);
        $rootScope.$digest();

        var classes = getClasses(element.find("label"));
        expect(classes).not.toContain("disabled");
    });

    it("Set label enabled for undefined binded value.", function () {
        var element = $compile('<form><input name="name" ng-model="name"/><q-label for="name" disabled="{{dd}}">Name</q-label></form>')($rootScope);
        $rootScope.$digest();

        var classes = getClasses(element.find("label"));
        expect(classes).not.toContain("disabled");
    });

    it("Set label enabled for undefined other binded value.", function () {
        $rootScope.dd = {};
        var element = $compile('<form><input name="name" ng-model="name"/><q-label for="name" disabled="{{dd}}">Name</q-label></form>')($rootScope);
        $rootScope.$digest();

        var classes = getClasses(element.find("label"));
        expect(classes).not.toContain("disabled");
    });

    it("Removes angular directive element.", function () {
        var element = $compile('<form><input name="name" ng-model="name"/><q-label for="name">Name</q-label></form>')($rootScope);
        $rootScope.$digest();

        expect(element.find("q-label").length).toEqual(0);
    });

    it("Watch disabled attribute.", function () {
        $rootScope.dd = true;
        var element = $compile('<form><input name="name" ng-model="name"/><q-label for="name" disabled="{{dd}}">Name</q-label></form>')($rootScope);
        $rootScope.$digest();

        var classes = getClasses(element.find("label"));
        expect(classes).toContain("disabled");

        $rootScope.dd = false;
        $rootScope.$digest();

        var classes = getClasses(element.find("label"));
        expect(classes).not.toContain("disabled");
    });

    it("Fails when no 'for' attribute specified.", function () {
        expect(function() {
            $compile('<form name="fName"><input name="name" ng-model="name"/><q-label>Name</q-label></form>')($rootScope);
        }).toThrowError("The qLabel directive in form 'fName' with text 'Name' should have 'for' attribute specified.");
    });

    it("Fails when not in form.", function () {
        expect(function () {
            $compile('<input name="name" ng-model="name"/><q-label>Name</q-label>')($rootScope);
        }).toThrowError(/\[\$compile:ctreq\][\s\S]*?\$compile\/ctreq\?p0=form&p1=qLabel/);
    });

    it("Fails for references to undefined input.", function () {
        expect(function() {
            $compile('<form name="fName"><input name="name" ng-model="name"/><q-label for="abra">Name</q-label></form>')($rootScope);
        }).toThrowError("The qLabel directive in form 'fName' with text 'Name' references and undefined input with name 'abra'.");
    });

    describe("Test input state.", function() {
        var $form, $input, $label, $classes;
        beforeEach(function() {
            $form = $compile('<form name="form" novalidate><input name="name" ng-model="name" required/><q-label for="name">Name</q-label></form>')($rootScope);
            $input = $form.find("input");
            $label = $form.find("label");
            $rootScope.$digest();
        });

        it("Initial input is invalid and pristine.", function () {
            var classes = getClasses($label);

            expect(classes).toContain("ng-invalid");
            expect(classes).toContain("ng-pristine");
        });

        it("Input becomes valid and dirty.", function () {
            $rootScope.form.name.$setViewValue("dd");
            var classes = getClasses($label);

            expect(classes).toContain("ng-valid");
            expect(classes).toContain("ng-dirty");
        });

        it("Input is valid and dirty after that it becomes invalid.", function () {
            $rootScope.form.name.$setViewValue("dd");
            var classes = getClasses($label);

            expect(classes).toContain("ng-valid");
            expect(classes).toContain("ng-dirty");

            $rootScope.form.name.$setViewValue("");
            var classes = getClasses($label);

            expect(classes).toContain("ng-invalid");
            expect(classes).toContain("ng-dirty");
        });

        it("Input is valid and dirty after that it becomes pristine.", function () {
            $rootScope.form.name.$setViewValue("dd");
            var classes = getClasses($label);

            expect(classes).toContain("ng-valid");
            expect(classes).toContain("ng-dirty");

            $rootScope.form.$setPristine();
            $rootScope.$digest();
            var classes = getClasses($label);

            expect(classes).toContain("ng-valid");
            expect(classes).toContain("ng-pristine");
        });

        it("Input becomes dirty.", function () {
            $rootScope.form.name.$setDirty();
            $rootScope.$digest();
            var classes = getClasses($label);

            expect(classes).toContain("ng-invalid");
            expect(classes).toContain("ng-dirty");
        });

    });

    function getClasses($label) {
        return $label.attr("class").split(/\s+/);
    }

});