describe("Testing q-dual-form-group directive.", function () {
    beforeEach(module('qitcommonsModule'));

    var $compile, $rootScope, $element;
    beforeEach(inject(function (_$compile_, _$rootScope_) {
        $compile = _$compile_;
        $rootScope = _$rootScope_;
    }));

    afterEach(function () {
        $element.remove();
    });

    it("Adds ng-form.", function () {
        $element = $compile("<div><q-dual-form-group><q-dual-form-group-view></q-dual-form-group-view></q-dual-form-group></div>")($rootScope);
        $rootScope.$digest();

        expect($element.find("ng-form").length).toEqual(1);
    });

    it("Adds form-group.", function () {
        $element = $compile("<div><q-dual-form-group><q-dual-form-group-view></q-dual-form-group-view></q-dual-form-group></div>")($rootScope);
        $rootScope.$digest();

        expect($element.find("ng-form>div.form-group").length).toEqual(1);
    });

    it("Adds label.", function () {
        $element = $compile('<div><q-dual-form-group label="Nam"><q-dual-form-group-view></q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
        $rootScope.$digest();

        var label = $element.find("ng-form label.col-sm-2.col-md-2.col-lg-2.control-label");
        expect(label.text()).toEqual("Nam");
        expect(label.length).toEqual(1);
    });

    it("Adds div instead of label when label text not specified.", function () {
        $element = $compile('<div><q-dual-form-group><q-dual-form-group-view></q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
        $rootScope.$digest();

        var label = $element.find("ng-form label.col-sm-2.col-md-2.col-lg-2.control-label");
        var div = $element.find("ng-form div.col-sm-2.col-md-2.col-lg-2");
        expect(label.length).toEqual(0);
        expect(div.length).toEqual(1);
    });

    it("Shows label.", function () {
        $element = $compile('<div><q-dual-form-group label="Nam" show-label="true"><q-dual-form-group-view></q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
        $rootScope.$digest();

        var label = $element.find("ng-form label.col-sm-2.col-md-2.col-lg-2.control-label");
        expect(label.text()).toEqual("Nam");
        expect(label.length).toEqual(1);
    });

    it("Hides label.", function () {
        $element = $compile('<div><q-dual-form-group label="Nam" show-label="false"><q-dual-form-group-view></q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
        $rootScope.$digest();

        var label = $element.find("ng-form label.col-sm-2.col-md-2.col-lg-2.control-label");
        var div = $element.find("ng-form div.col-sm-2.col-md-2.col-lg-2");
        expect(label.length).toEqual(0);
        expect(div.length).toEqual(1);
    });

    it("Adds css classes.", function () {
        $element = $compile('<div><q-dual-form-group class="a b"><q-dual-form-group-view></q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ng-form>div.form-group.a.b").length).toEqual(1);
    });

    it("Adds css classes as array.", function () {
        $rootScope._classes = ["a", "b"]
        $element = $compile('<div><q-dual-form-group class="{{_classes}}"><q-dual-form-group-view></q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ng-form>div.form-group.a.b").length).toEqual(1);
    });

    it("Hides when no child panels.", function () {
        $element = $compile('<div><q-dual-form-group></q-dual-form-group></div>')($rootScope);
        $rootScope.$digest();
        $(document.body).append($element);

        expect($element.find("div.form-group:hidden").length).toEqual(1);
    });

    it("Hides when only edit panel.", function () {
        $element = $compile('<div><q-dual-form-group><q-dual-form-grou-edit>ddd</q-dual-form-grou-edit></q-dual-form-group></div>')($rootScope);
        $rootScope.$digest();
        $(document.body).append($element);

        expect($element.find("div.form-group:hidden").length).toEqual(1);
    });

    it("Visible when view panel exists.", function () {
        $element = $compile('<div><q-dual-form-group><q-dual-form-group-view>ddd</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
        $rootScope.$digest();
        $(document.body).append($element);

        expect($element.find("div.form-group:visible").length).toEqual(1);
    });

    it("Names ng-form.", function () {
        $element = $compile('<div><q-dual-form-group name="formNam"><q-dual-form-group-view>ddd</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ng-form").attr('name')).toEqual('formNam');
    });

    it("Names anonymous ng-form with uuid.", function () {
        $element = $compile('<div><q-dual-form-group><q-dual-form-group-view>ddd</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
        $rootScope.$digest();

        expect($element.find("ng-form").attr('name')).toMatch(/^form_[0-9a-f]{32}$/i);
    });

    it("Sets in parent scope form angular object.", function () {
        $element = $compile('<div><q-dual-form-group name="formNam"><q-dual-form-group-view>ddd</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
        $rootScope.$digest();

        expect($rootScope.formNam).not.toBeUndefined();
        expect($rootScope.formNam.$name).toEqual("formNam");
    });

    it("Default shows view panel.", function () {
        $element = $compile('<div><q-dual-form-group name="formNam">' +
            '<q-dual-form-group-view><div class="view">view</div></q-dual-form-group-view>' +
            '<q-dual-form-group-edit><div class="edit">edit</div></q-dual-form-group-edit>' +
            '</q-dual-form-group></div>')($rootScope);
        $(document.body).append($element);
        $rootScope.$digest();

        expect($element.find('div.view:visible').length).toEqual(1);
        expect($element.find('div.edit:hidden').length).toEqual(1);
    });

    it("Null edit mode shows view panel.", function () {
        $rootScope._edit = null;
        $element = $compile('<div><q-dual-form-group name="formNam" edit-mode="{{_edit}}">' +
            '<q-dual-form-group-view><div class="view">view</div></q-dual-form-group-view>' +
            '<q-dual-form-group-edit><div class="edit">edit</div></q-dual-form-group-edit>' +
            '</q-dual-form-group></div>')($rootScope);
        $(document.body).append($element);
        $rootScope.$digest();

        expect($element.find('div.view:visible').length).toEqual(1);
        expect($element.find('div.edit:hidden').length).toEqual(1);
    });

    it("Empty edit mode shows view panel.", function () {
        $element = $compile('<div><q-dual-form-group name="formNam" edit-mode="">' +
            '<q-dual-form-group-view><div class="view">view</div></q-dual-form-group-view>' +
            '<q-dual-form-group-edit><div class="edit">edit</div></q-dual-form-group-edit>' +
            '</q-dual-form-group></div>')($rootScope);
        $(document.body).append($element);
        $rootScope.$digest();

        expect($element.find('div.view:visible').length).toEqual(1);
        expect($element.find('div.edit:hidden').length).toEqual(1);
    });

    it("Initially in edit mode.", function () {
        $element = $compile('<div><q-dual-form-group name="formNam" edit-mode="true">' +
            '<q-dual-form-group-view><div class="view">view</div></q-dual-form-group-view>' +
            '<q-dual-form-group-edit><div class="edit">edit</div></q-dual-form-group-edit>' +
            '</q-dual-form-group></div>')($rootScope);
        $(document.body).append($element);
        $rootScope.$digest();

        expect($element.find('div.view:hidden').length).toEqual(1);
        expect($element.find('div.edit:visible').length).toEqual(1);
    });

    it("Replaces angular element.", function() {
        $element = $compile('<div><q-dual-form-group name="formNam" edit-mode="true">' +
            '<q-dual-form-group-view><div class="view">view</div></q-dual-form-group-view>' +
            '<q-dual-form-group-edit><div class="edit">edit</div></q-dual-form-group-edit>' +
            '</q-dual-form-group></div>')($rootScope);
        $(document.body).append($element);
        $rootScope.$digest();

        expect($element.find('q-dual-form-group').length).toEqual(0);
    });

    it("Replaces angular panel elements.", function() {
        $element = $compile('<div><q-dual-form-group name="formNam" edit-mode="true">' +
            '<q-dual-form-group-view><div class="view">view</div></q-dual-form-group-view>' +
            '<q-dual-form-group-edit><div class="edit">edit</div></q-dual-form-group-edit>' +
            '</q-dual-form-group></div>')($rootScope);
        $(document.body).append($element);
        $rootScope.$digest();

        expect($element.find('q-dual-form-group-view').length).toEqual(0);
        expect($element.find('q-dual-form-group-edit').length).toEqual(0);
    });

    describe("Check listeners.", function () {
        beforeEach(function () {
            $rootScope._showLabel = true;
            $rootScope._label = "Nam";
            $element = $compile('<div><q-dual-form-group name="formNam" show-label="{{_showLabel}}" label="{{_label}}"><q-dual-form-group-view>ddd</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
            $rootScope.$digest();
        });

        it("Modifies showLabel.", function () {
            $rootScope._showLabel = false;
            $rootScope.$digest();

            var label = $element.find("ng-form label.col-sm-2.col-md-2.col-lg-2.control-label");
            var div = $element.find("ng-form div.col-sm-2.col-md-2.col-lg-2");
            expect(label.length).toEqual(0);
            expect(div.length).toEqual(1);
        });

        it("Modifies showLabel (hides and shows again).", function () {
            $rootScope._showLabel = false;
            $rootScope.$digest();
            $rootScope._showLabel = true;
            $rootScope.$digest();

            var label = $element.find("ng-form label.col-sm-2.col-md-2.col-lg-2.control-label");
            var div = $element.find("ng-form div.col-sm-2.col-md-2.col-lg-2");
            expect(label.length).toEqual(1);
            expect(div.length).toEqual(0);
        });

        it("Sets label to empty string.", function () {
            $rootScope._label = "";
            $rootScope.$digest();

            var label = $element.find("ng-form label.col-sm-2.col-md-2.col-lg-2.control-label");
            var div = $element.find("ng-form div.col-sm-2.col-md-2.col-lg-2");
            expect(label.length).toEqual(0);
            expect(div.length).toEqual(1);
        });

        it("Sets label to null.", function () {
            $rootScope._label = null;
            $rootScope.$digest();

            var label = $element.find("ng-form label.col-sm-2.col-md-2.col-lg-2.control-label");
            var div = $element.find("ng-form div.col-sm-2.col-md-2.col-lg-2");
            expect(label.length).toEqual(0);
            expect(div.length).toEqual(1);
        });

        it("Sets label to undefnied.", function () {
            $rootScope._label = undefined;
            $rootScope.$digest();

            var label = $element.find("ng-form label.col-sm-2.col-md-2.col-lg-2.control-label");
            var div = $element.find("ng-form div.col-sm-2.col-md-2.col-lg-2");
            expect(label.length).toEqual(0);
            expect(div.length).toEqual(1);
        });
    });

    describe("Check edit mode switching.", function () {
        it("Displays edit panel.", function () {
            $rootScope._editMode = false;
            $element = $compile('<div><q-dual-form-group name="formNam" edit-mode="{{_editMode}}">' +
                '<q-dual-form-group-view><div class="view">view</div></q-dual-form-group-view>' +
                '<q-dual-form-group-edit><div class="edit">edit</div></q-dual-form-group-edit>' +
                '</q-dual-form-group></div>')($rootScope);
            $(document.body).append($element);
            $rootScope.$digest();

            $rootScope._editMode = true;
            $rootScope.$digest();

            expect($element.find('div.view:hidden').length).toEqual(1);
            expect($element.find('div.edit:visible').length).toEqual(1);
        });

        it("Displays and hides edit panel.", function () {
            $rootScope._editMode = false;
            $element = $compile('<div><q-dual-form-group name="formNam" edit-mode="{{_editMode}}">' +
                '<q-dual-form-group-view><div class="view">view</div></q-dual-form-group-view>' +
                '<q-dual-form-group-edit><div class="edit">edit</div></q-dual-form-group-edit>' +
                '</q-dual-form-group></div>')($rootScope);
            $(document.body).append($element);
            $rootScope.$digest();

            $rootScope._editMode = true;
            $rootScope.$digest();
            expect($element.find('div.view:hidden').length).toEqual(1);
            expect($element.find('div.edit:visible').length).toEqual(1);

            $rootScope._editMode = false;
            $rootScope.$digest();
            expect($element.find('div.view:visible').length).toEqual(1);
            expect($element.find('div.edit:hidden').length).toEqual(1);
        });

        it("Displays and hides multiple edit and view panels.", function () {
            $rootScope._editMode = false;
            $element = $compile('<div><q-dual-form-group name="formNam" edit-mode="{{_editMode}}">' +
                '<q-dual-form-group-view><div class="view">view</div></q-dual-form-group-view>' +
                '<q-dual-form-group-view><div class="view">view</div></q-dual-form-group-view>' +
                '<q-dual-form-group-edit><div class="edit">edit</div></q-dual-form-group-edit>' +
                '<q-dual-form-group-edit><div class="edit">edit</div></q-dual-form-group-edit>' +
                '</q-dual-form-group></div>')($rootScope);
            $(document.body).append($element);
            $rootScope.$digest();

            $rootScope._editMode = true;
            $rootScope.$digest();
            expect($element.find('div.view:hidden').length).toEqual(2);
            expect($element.find('div.edit:visible').length).toEqual(2);

            $rootScope._editMode = false;
            $rootScope.$digest();
            expect($element.find('div.view:visible').length).toEqual(2);
            expect($element.find('div.edit:hidden').length).toEqual(2);
        });

        it("Hides form group on editing when no edit panel present.", function () {
            $rootScope._editMode = false;
            $element = $compile('<div><q-dual-form-group name="formNam" edit-mode="{{_editMode}}">' +
                '<q-dual-form-group-view><div class="view">view</div></q-dual-form-group-view>' +
                '</q-dual-form-group></div>')($rootScope);
            $(document.body).append($element);
            $rootScope.$digest();

            $rootScope._editMode = true;
            $rootScope.$digest();

            expect($element.find('div.form-group:hidden').length).toEqual(1);
        });

        it("Shows form group on editing when no view panel present.", function () {
            $rootScope._editMode = false;
            $element = $compile('<div><q-dual-form-group name="formNam" edit-mode="{{_editMode}}">' +
                '<q-dual-form-group-edit><div class="edit">edit</div></q-dual-form-group-edit>' +
                '</q-dual-form-group></div>')($rootScope);
            $(document.body).append($element);
            $rootScope.$digest();

            $rootScope._editMode = true;
            $rootScope.$digest();

            expect($element.find('div.form-group:visible').length).toEqual(1);
            expect($element.find('div.edit:visible').length).toEqual(1);
        });

        it("Shows form group on editing when no edit panel present.", function () {
            $rootScope._editMode = true;
            $element = $compile('<div><q-dual-form-group name="formNam" edit-mode="{{_editMode}}">' +
                '<q-dual-form-group-view><div class="view">view</div></q-dual-form-group-view>' +
                '</q-dual-form-group></div>')($rootScope);
            $(document.body).append($element);
            $rootScope.$digest();

            $rootScope._editMode = false;
            $rootScope.$digest();

            expect($element.find('div.form-group:visible').length).toEqual(1);
            expect($element.find('div.view:visible').length).toEqual(1);
        });

        it("Hides form group on editing when no view panel present.", function () {
            $rootScope._editMode = true;
            $element = $compile('<div><q-dual-form-group name="formNam" edit-mode="{{_editMode}}">' +
                '<q-dual-form-group-edit><div class="edit">edit</div></q-dual-form-group-edit>' +
                '</q-dual-form-group></div>')($rootScope);
            $(document.body).append($element);
            $rootScope.$digest();

            $rootScope._editMode = false;
            $rootScope.$digest();

            expect($element.find('div.form-group:hidden').length).toEqual(1);
            expect($element.find('div.edit:hidden').length).toEqual(1);
        });
    });

    describe("Check panels.", function () {
        it("Sets the width of the panel.", function () {
            $element = $compile('<div><q-dual-form-group name="formNam"><q-dual-form-group-view width="2">view</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
            $rootScope.$digest();

            expect($element.find("div.q-dual-form-group-view.col-lg-2.col-md-2.col-sm-2").length).toEqual(1);
        });

        it("Sets the width default to 6.", function () {
            $element = $compile('<div><q-dual-form-group name="formNam"><q-dual-form-group-view>view</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
            $rootScope.$digest();

            expect($element.find("div.q-dual-form-group-view.col-lg-6.col-md-6.col-sm-6").length).toEqual(1);
        });

        it("Sets the width default to 6 on passed null.", function () {
            $rootScope._width = null;
            $element = $compile('<div><q-dual-form-group name="formNam"><q-dual-form-group-view width="{{_width}}">view</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
            $rootScope.$digest();

            expect($element.find("div.q-dual-form-group-view.col-lg-6.col-md-6.col-sm-6").length).toEqual(1);
        });

        it("Sets the width default to 6 on passed empty string.", function () {
            $element = $compile('<div><q-dual-form-group name="formNam"><q-dual-form-group-view width="">view</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
            $rootScope.$digest();

            expect($element.find("div.q-dual-form-group-view.col-lg-6.col-md-6.col-sm-6").length).toEqual(1);
        });

        it("Sets the offset of the panel.", function () {
            $element = $compile('<div><q-dual-form-group name="formNam"><q-dual-form-group-view offset="2">view</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
            $rootScope.$digest();

            expect($element.find("div.q-dual-form-group-view.col-lg-offset-2.col-md-offset-2.col-sm-offset-2").length).toEqual(1);
        });

        it("Do not set the offset by default.", function () {
            $element = $compile('<div><q-dual-form-group name="formNam"><q-dual-form-group-view>view</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
            $rootScope.$digest();

            expect($element.find("div.q-dual-form-group-view[class*='col-lg-offset-'][class*='col-md-offset-'][class*='col-sm-offset-']").length).toEqual(0);
        });

        it("Do not set the offset by default if it is set to null.", function () {
            $rootScope._offset = null;
            $element = $compile('<div><q-dual-form-group name="formNam"><q-dual-form-group-view offset="{{_offset}}">view</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
            $rootScope.$digest();

            expect($element.find("div.q-dual-form-group-view[class*='col-lg-offset-'][class*='col-md-offset-'][class*='col-sm-offset-']").length).toEqual(0);
        });

        it("Do not set the offset by default if it is set to empty string.", function () {
            $element = $compile('<div><q-dual-form-group name="formNam"><q-dual-form-group-view offset="">view</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
            $rootScope.$digest();

            expect($element.find("div.q-dual-form-group-view[class*='col-lg-offset-'][class*='col-md-offset-'][class*='col-sm-offset-']").length).toEqual(0);
        });

        describe("Listen for the width and offset changes.", function() {
           beforeEach(function() {
               $rootScope._width = null;
               $rootScope._offset = null;
               $element = $compile('<div><q-dual-form-group name="formNam"><q-dual-form-group-view width="{{_width}}" offset="{{_offset}}">view</q-dual-form-group-view></q-dual-form-group></div>')($rootScope);
               $rootScope.$digest();
           });

            it("Modifies width.", function() {
                $rootScope._width = 2;
                $rootScope.$digest();

                expect($element.find("div.q-dual-form-group-view.col-lg-6.col-md-6.col-sm-6").length).toEqual(0);
                expect($element.find("div.q-dual-form-group-view.col-lg-2.col-md-2.col-sm-2").length).toEqual(1);
            });

            it("Modifies offset.", function() {
                $rootScope._offset = 1;
                $rootScope.$digest();
                expect($element.find("div.q-dual-form-group-view.col-lg-offset-1.col-md-offset-1.col-sm-offset-1").length).toEqual(1);

                $rootScope._offset = 2;
                $rootScope.$digest();

                expect($element.find("div.q-dual-form-group-view.col-lg-offset-1.col-md-offset-1.col-sm-offset-1").length).toEqual(0);
                expect($element.find("div.q-dual-form-group-view.col-lg-offset-2.col-md-offset-2.col-sm-offset-2").length).toEqual(1);
            });
        });
    });
});
