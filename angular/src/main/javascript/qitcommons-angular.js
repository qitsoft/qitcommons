var qitcommonsModule = angular.module("qitcommonsModule", []);

qitcommonsModule.service("$qListController", ["$location", "$timeout", function ($location, $timeout) {
    return function(scope, config) {
        this.queryTimer = undefined;
        var filterName = (config.name + "Filter");

        scope.page = config.page;
        if (!angular.isNumber(scope.page)) {
            if (!isNaN(parseInt($location.search().p))) {
                scope.page = parseInt($location.search().p)
            } else {
                scope.page = 1
            }
        }
        scope.pageSize = config.pageSize || 10;
        scope.query = $location.search().q;
        var listParams = {query: scope.query, page: scope.page, pageSize: scope.pageSize};
        listParams = angular.extend(listParams, config.params);
        scope.list = config.service(listParams, config.success, config.error);

        if (config.filter) {
            if (typeof config.filter == "string") {
                $(config.filter).focus();
            } else if (config.filter.focus) {
                config.filter.focus();
            }
        }

        scope.clearFilter = function () {
            $timeout.cancel(this.queryTimer);
            scope.query = null;
            $location.search("q", undefined);
        };

        scope.typingFilter = function (query) {
            $timeout.cancel(this.queryTimer);
            this.queryTimer = $timeout(function () {
                scope.filter(query);
            }, config.typingTimeout || 1000);
        };

        scope.filter = function (query) {
            $timeout.cancel(this.queryTimer);
            if (angular.isDefined(query)) {
                scope.query = query;
            }
            if (typeof scope.query == "string" || scope.query == null) {
                $location.search("p", undefined);

                if (scope.query) {
                    $location.search("q", scope.query);
                } else {
                    $location.search("q", undefined);
                }
            }
        }
    }
}]);

qitcommonsModule.directive("qPager", ["$location", "$parse", function ($location) {
    return {
        restrict: "E",
        replace: true,
        template: '<ul class="q-pager pagination">' +
        '<li ng-repeat="p in pages" ng-class="{active: p.page == page, disabled: !p.clicakble}" ng-show="total>1">' +
        '<span ng-if="!p.clickable">{{p.page}}</span>' +
        '<a ng-if="p.clickable" href="javascript:void(0);" ng-click="p.action()">{{p.page}}</a>' +
        '</li>' +
        '</ul>',
        scope: {
            items: '@',
            page: "@",
            pageSize: "@",
            total: "@",
            change: "&"
        },
        link: function (scope, element, attrs) {

            var move = function(page) {
                if (attrs.change) {
                    scope.change({$page: page, $oldPage: scope.page});
                    scope.page = page;
                } else {
                    if (page == 1) {
                        $location.search("p", null);
                    } else {
                        $location.search("p", page);
                    }
                }
            };

            var pageItem = function(name, clickable, page) {
                return {
                    page: name,
                    clickable: clickable,
                    action: function() {
                        move(page);
                    }
                }
            };

            var update = function() {
                scope.page = parseInt(scope.page);
                if (isNaN(scope.page)) {
                    scope.page = parseInt($location.search().p);
                }
                if (isNaN(scope.page)) {
                    scope.page = 1;
                }

                if (angular.isDefined(scope.total)) {
                    scope.calculatedTotal = parseInt(scope.total);
                } else if (angular.isDefined(scope.items)) {
                    scope.items = parseInt(scope.items);
                    scope.pageSize = parseInt(scope.pageSize);
                    if (isNaN(scope.pageSize)) {
                        throw new Error("Page size should be defined and should be a number in component qPager.");
                    }
                    if (scope.pageSize <= 0) {
                        throw new Error("Page size should be greater than 0 in component qPager.");
                    }
                    scope.calculatedTotal = Math.ceil(scope.items / scope.pageSize);
                }
                if (scope.page > scope.calculatedTotal) {
                    move(scope.calculatedTotal);
                    return;
                }
                if (scope.page < 1) {
                    move(1);
                    return;
                }

                var lowerEdge = scope.page - 4;
                var upperEdge = scope.page + 4;
                if (scope.calculatedTotal == 10) {
                    lowerEdge = 1;
                    upperEdge = 10;
                } else {
                    if (lowerEdge <= 0) {
                        lowerEdge = 1;
                        upperEdge = lowerEdge + 8;
                    }
                    if (upperEdge > scope.calculatedTotal) {
                        upperEdge = scope.calculatedTotal;
                        lowerEdge = upperEdge - 8;
                    }
                    if (lowerEdge <= 0) {
                        lowerEdge = 1;
                    }
                }

                scope.pages = [];
                scope.pages.push(pageItem("«", scope.page > 1, scope.page - 1));
                if (lowerEdge > 1) {
                    scope.pages.push(pageItem(1, true, 1));
                    scope.pages.push(pageItem("…", false));
                }
                for(var i=lowerEdge;i<=upperEdge;i++) {
                    scope.pages.push(pageItem(i, true, i));
                }
                if (upperEdge < scope.calculatedTotal) {
                    scope.pages.push(pageItem("…", false));
                    scope.pages.push(pageItem(scope.calculatedTotal, true, scope.calculatedTotal));
                }
                scope.pages.push(pageItem("»", scope.page < scope.calculatedTotal, scope.page + 1));
            };

            scope.$watch("page", update);
            scope.$watch("total", update);
            scope.$watch("items", update);
            scope.$watch("pageSize", update);
            update();
        }
    }
}]);

qitcommonsModule.directive("qDualFormGroup", ["$qUtils", function ($qUtils) {
    return {
        restrict: "E",
        transclude: true,
        replace: true,
        scope: {
            label: "@",
            editMode: "@",
            showLabel: "@",
            name: "@"
        },
        template: function (element, attrs) {
            attrs.name = attrs.name || "form_" + $qUtils.uuid().replace(/[^0-9a-fA-F]/g, "");
            return '<ng-form name="' + attrs.name + '">' +
                '<div class="form-group" ng-class="styles">' +
                '<label ng-if="labelExists" class="col-sm-2 col-md-2 col-lg-2 control-label">{{label}}</label>' +
                '<div ng-if="!labelExists" class="col-sm-2 col-md-2 col-lg-2"></div>' +
                '<div ng-transclude>' +
                '</div>' +
                '</div>' +
                '</ng-form>';
        },
        link: function($scope, $element, $attrs) {
            var editModeListener = function () {
                var editMode = $qUtils.bool($scope.editMode, false);
                var viewPanels = $element.find(".q-dual-form-group-view");
                var editPanels = $element.find(".q-dual-form-group-edit");

                if (editMode) {
                    if (editPanels.length == 0) {
                        $element.hide();
                    } else {
                        $element.show();
                        viewPanels.hide();
                        editPanels.show();
                    }
                } else {
                    if (viewPanels.length == 0) {
                        $element.hide();
                    } else {
                        $element.show();
                        editPanels.hide();
                        viewPanels.show();
                    }
                }
            };

            var domUpdateListener = function (e) {
                if (e.target == e.currentTarget || e.target.parentElement != e.currentTarget) {
                    return;
                }
                editModeListener();
            };

            var updateLabel = function(value) {
                if (angular.isUndefined($scope.label) || $scope.label == null || $.trim($scope.label) == '') {
                    $scope.labelExists = false;
                } else {
                    $scope.labelExists = $qUtils.bool($scope.showLabel, true);
                }
            };

            $scope.$watch("label", updateLabel);
            $scope.$watch("showLabel", updateLabel);

            $scope.$watch(function() {
                return $element.attr('class');
            }, function(value) {
                if (typeof value == "string") {
                    var classes = $.trim(value);
                    if (classes[0] == '[' && classes[classes.length - 1] == ']') {
                        $scope.styles = $.map($.trim(classes.substring(1, classes.length - 1)).split(/[\s,]+/), function(e){
                            return e.replace(/["']/g, "");
                        });
                    } else {
                        $scope.styles = classes.split(/[\s]+/);
                    }
                } else {
                    $scope.styles = [];
                }
            });

            if ($scope.name && $scope.name != "") {
                $scope.$watch($scope.name, function (value) {
                    $scope.$parent[$scope.name] = value;
                });
                $scope.$parent[$scope.name] = $scope.name;
            }
            $scope.$watch("editMode", editModeListener);

            var transcludeElement = $element.find("div[ng-transclude]");
            transcludeElement.bind("DOMNodeInserted", domUpdateListener);
            transcludeElement.bind("DOMNodeRemoved", domUpdateListener);

            $scope.editMode = $qUtils.bool($scope.editMode, false);
        },
        controller: function ($scope, $element, $attrs, $transclude) {
        }
    }
}]);

var qDualFormGroupItem = function (template, type) {
    return {
        require: "^qDualFormGroup",
        restrict: "E",
        replace: true,
        transclude: true,
        scope: {
            offset: "@",
            width: "@"
        },
        template: template,
        link: function (scope, element, attrs, ctrl) {
            scope.$watch("width", function(value, old){
                scope.width = scope.width || 6;
                element.removeClass("col-sm-" + old);
                element.removeClass("col-md-" + old);
                element.removeClass("col-lg-" + old);

                element.addClass("col-sm-" + value);
                element.addClass("col-md-" + value);
                element.addClass("col-lg-" + value);
            });
            scope.$watch("offset", function(value, old) {
                element.removeClass("col-sm-offset-" + old);
                element.removeClass("col-md-offset-" + old);
                element.removeClass("col-lg-offset-" + old);

                if (value) {
                    element.addClass("col-sm-offset-" + scope.offset);
                    element.addClass("col-md-offset-" + scope.offset);
                    element.addClass("col-lg-offset-" + scope.offset);
                }
            });
        }
    }
};

qitcommonsModule.directive("qDualFormGroupView", function () {
    return qDualFormGroupItem('<div class="q-dual-form-group-view"><p class="form-control-static" ng-transclude></p></div>');
});

qitcommonsModule.directive("qDualFormGroupEdit", function () {
    return qDualFormGroupItem('<div class="q-dual-form-group-edit" ng-transclude></div>');
});

qitcommonsModule.directive("qErrorMessage", function () {
    return {
        require: "^form",
        restrict: "E",
        transclude: true,
        replace: true,
        scope: {
            "for": "@",
            "error": "@"
        },
        template: '<span class="q-error-message help-block" ng-transclude></span>',
        link: function (scope, element, attrs, ctrl) {
            if (!attrs['for']) {
                throw new Error("The qErrorMessage directive in form '" + ctrl.$name + "' with text '" + element.text() + "' should have 'for' attribute specified.");
            }
            if (!ctrl[attrs['for']]) {
                throw new Error("The qErrorMessage directive in form '" + ctrl.$name + "' with text '" + element.text() + "' references and undefined input with name '" + attrs['for'] + "'.");
            }
            if (!attrs.error) {
                throw new Error("The qErrorMessage directive in form '" + ctrl.$name + "' with text '" + element.text() + "' should have 'error' attribute specified.");
            }
            element.hide();
            scope.$watch(function () {
                return ctrl[attrs['for']].$error[attrs.error];
            }, function (value) {
                if (value) {
                    element.fadeIn();
                } else {
                    element.fadeOut();
                }
            });
            scope.$watch(function () {
                return ctrl[attrs['for']].$dirty;
            }, function (value) {
                element.toggleClass("ng-dirty", value);
            });
            scope.$watch(function () {
                return ctrl[attrs['for']].$pristine;
            }, function (value) {
                element.toggleClass("ng-pristine", value);
            });
            scope.$watch(function () {
                return ctrl[attrs['for']].$invalid;
            }, function (value) {
                element.toggleClass("ng-invalid", value);
            });
        }
    };
});

qitcommonsModule.directive("qLabel", ["$qUtils", function ($qUtils) {
    return {
        require: "^form",
        restrict: "E",
        transclude: true,
        replace: true,
        scope: {
            "for": "@",
            "disabled": "@",
            "class": "@"
        },
        template: '<label class="q-label control-label" ng-transclude></label>',
        link: function (scope, element, attrs, ctrl) {
            if (!attrs['for']) {
                throw new Error("The qLabel directive in form '" + ctrl.$name + "' with text '" + element.text() + "' should have 'for' attribute specified.");
            }
            if (!ctrl[attrs['for']]) {
                throw new Error("The qLabel directive in form '" + ctrl.$name + "' with text '" + element.text() + "' references and undefined input with name '" + attrs['for'] + "'.");
            }
            scope.$watch("disabled", function (value) {
                if ($qUtils.bool(value, false)) {
                    element.addClass("disabled");
                } else {
                    element.removeClass("disabled");
                }
            });
            scope.$watch(function () {
                return ctrl[attrs['for']].$valid;
            }, function (value) {
                element.toggleClass("ng-valid", value);
            });
            scope.$watch(function () {
                return ctrl[attrs['for']].$dirty;
            }, function (value) {
                element.toggleClass("ng-dirty", value);
            });
            scope.$watch(function () {
                return ctrl[attrs['for']].$pristine;
            }, function (value) {
                element.toggleClass("ng-pristine", value);
            });
            scope.$watch(function () {
                return ctrl[attrs['for']].$invalid;
            }, function (value) {
                element.toggleClass("ng-invalid", value);
            });
        }
    };
}]);

qitcommonsModule.service("$qAlert", ["$timeout", function ($timeout) {

    var show = function (args, type) {
        var statusPanel = $('<div class="status-panel alert alert-dismissible fade in" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button><span class="alert-body"></span></div>');
        var statusBody = $(".alert-body", statusPanel);

        var finalMessage = args[0];
        var messageParams = args[1];
        if (args.length > 1) {
            messageParams = Array.prototype.slice.call(args, 1)
        }

        var timeout = 10000;
        if (messageParams) {
            for (var i=0;i<messageParams.length;i++) {
                if (finalMessage.indexOf("{" + i + "}") >= 0) {
                    finalMessage = finalMessage.replace("{" + i + "}", messageParams[i]);
                }
            }
            if (messageParams.length > 0 && typeof  messageParams[messageParams.length - 1] === "number") {
                timeout = messageParams[messageParams.length - 1];
            }
        }
        statusPanel.addClass("alert-" + type);
        statusBody.html(finalMessage);
        $(document.body).prepend(statusPanel);
        statusPanel.alert();
        $timeout(function () {
            statusPanel.alert('close');
        }, timeout);
    };

    return {
        success: function (message, params, timeout) {
            show(arguments, "success");
        },
        info: function (message, params, timeout) {
            show(arguments, "info");
        },
        warning: function (message, params, timeout) {
            show(arguments, "warning");
        },
        danger: function (message, params, timeout) {
            show(arguments, "danger");
        }
    };
}]);

qitcommonsModule.directive('qMagnificPopup', function () {
    var expectedOptions = function () {
        var $string = function (value) {
            return value;
        };
        var $bool = function (value) {
            value = value || "false";
            value = value.toLowerCase();
            if (value == "true" || value == "yes" || value == "t" || value == "y" || value == "1" || value == "on") {
                return true
            } else if (value == "false" || value == "no" || value == "f" || value == "n" || value == "0" || value == "off") {
                return false;
            }
            return undefined;
        };
        var $int = function (value) {
            var intVal = Integer.parseInt(value);
            if (intVal == NaN) {
                return undefind;
            } else {
                return intVal;
            }
        };
        var $func = function (value) {

        };
        var $dom = function (value) {

        };
        var $arr = function (value) {

        };
        var $mix = function () {
            var mixArgs = arguments;
            return function (value) {
                for (var i = 0; i < mixArgs.length; i++) {
                    var result = mixArgs[i](value);
                    if (result != undefined) {
                        return result;
                    }
                }
                return undefined;
            }
        };

        var rawOtions = {
            "delegate": $string,
            "type": $string,
            "disableOn": $mix($int, $func),
            "key": $string,
            "mainClass": $string,
            "midClick": $bool,
            "preloader": $bool,
            "focus": $string,
            "closeOnContentClick": $bool,
            "closeOnBgClick": $bool,
            "closeBtnInside": $bool,
            "showCloseBtn": $bool,
            "enableEscapeKey": $bool,
            "modal": $bool,
            "alignTop": $mix($bool, $string),
            "fixedContentPos": $mix($bool, $string),
            "index": $int,
            "fixedBgPos": $mix($bool, $string),
            "overflowY": $string,
            "removalDelay": $int,
            "closeMarkup": $string,
            "prependTo": $dom,
            "gallery.enabled": $bool,
            "gallery.preload": $arr,
            "gallery.navigateByImgClick": $bool,
            "gallery.arrowMarkup": $string,
            "gallery.tPrev": $string,
            "gallery.tNext": $string,
            "gallery.tCounter": $string,
            "retina.ratio": $mix($int, $func),
            "retina.replaceSrc": $func,
            "zoom.enabled": $bool,
            "zoom.duration": $string,
            "zoom.easing": $int,
            "zoom.opener": $func,
            "callbacks.open": $func,
            "callbacks.close": $func,
            "callbacks.beforeOpen": $func,
            "callbacks.elementParse": $func,
            "callbacks.change": $func,
            "callbacks.resize": $func,
            "callbacks.open": $func,
            "callbacks.beforeClose": $func,
            "callbacks.afterClose": $func,
            "callbacks.markupParse": $func,
            "callbacks.updateStatus": $func,
            "callbacks.parseAjax": $func,
            "callbacks.ajaxContentAdded": $func
        };
        var optionsByAttribute = {};


        for (var k in rawOtions) {
            var attribute = k.replace(/\./g, "").toLowerCase();
            optionsByAttribute[attribute] = {
                path: k.split("."),
                parser: rawOtions[k]
            }
        }

        return {
            setOptionByAttribute: function (options, attr, value) {
                var option = optionsByAttribute[attr.toLowerCase()];
                if (option) {
                    var currentOptionsObject = options;
                    for (var i = 0; i < option.path.length; i++) {
                        var path = option.path[i];
                        if (i != (option.path.length - 1)) {
                            currentOptionsObject[path] = {};
                            currentOptionsObject = currentOptionsObject[path];
                        } else {
                            currentOptionsObject[path] = option.parser(value);
                        }
                    }
                }
            }
        };
    }();

    return {
        restrict: 'A',
        link: function ($scope, element, attrs) {
            var options = {};
            for (var attr in attrs) {
                if (attr.match(/magnificPopup.+/)) {
                    var attrName = attr.replace(/qMagnificPopup(.)/, function (a, g1) {
                        return g1.toLowerCase();
                    });

                    expectedOptions.setOptionByAttribute(options, attrName, attrs[attr]);
                }
            }

            element.magnificPopup(options);
        }
    }
});

qitcommonsModule.service("$qUtils", function () {
    return {
        bool: function (value, def) {
            def = def || false;
            var result = value === undefined || value === null ? def : value;
            if (typeof result === "boolean") {
                return result;
            } else if (typeof result === "string") {
                result = result.toLowerCase();
                return result === "true"
                    || result === "yes"
                    || result === "on"
                    || result === "t"
                    || result === "y"
                    || result === "1";
            } else if (typeof result === "number") {
                return !isNaN(result)
                    && result !== Number.NEGATIVE_INFINITY
                    && result !== Number.POSITIVE_INFINITY
                    && result != 0;
            } else if (Array.isArray(result)) {
                return result.length > 0;
            } else if (typeof result === "object") {
                return true;
            } else {
                return def;
            }
        },

        uuid: function () {
            var d = new Date().getTime();
            var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
            return uuid;
        }
    };
});