describe("Test $qAlert service.", function() {
    var $compile, $qAlert, $timeout, $body;

    beforeEach(function() {
        module("qitcommonsModule");

        inject(function(_$compile_, _$qAlert_, _$timeout_) {
            $compile = _$compile_;
            $qAlert = _$qAlert_;
            $timeout = _$timeout_;
        });

        $body = $(document.body);
        spyOn($.fn, "alert");
    });

    afterEach(function() {
        $("div.status-panel.alert.alert-dismissible", $body).remove();
    });

    it("Adds alert to body element.", function() {
        $qAlert.success("the-message");

        expect($body.find("div.status-panel.alert.alert-dismissible[role='alert']").length).toEqual(1);
    });

    it("Has close button.", function() {
        $qAlert.success("the-message");

        var closeButton = $body.find("div.status-panel.alert.alert-dismissible[role='alert'] button.close[data-dismiss='alert']");
        expect(closeButton.length).toEqual(1);
        expect(closeButton.text()).toMatch(new RegExp("("+$('<span>&times;</span>').text()+"|&times;)"));
    });

    it("Has content set.", function() {
        $qAlert.success("the-message");

        var alertBody = $body.find("div.status-panel.alert.alert-dismissible[role='alert'] span.alert-body");
        expect(alertBody.length).toEqual(1);
        expect(alertBody.text()).toMatch("the-message");
    });

    it("success() call sets alert-success style class.", function() {
        $qAlert.success("the-message");

        expect($body.find("div.status-panel.alert.alert-success").length).toEqual(1);
        expect($body.find("div.status-panel.alert.alert-info").length).toEqual(0);
        expect($body.find("div.status-panel.alert.alert-warning").length).toEqual(0);
        expect($body.find("div.status-panel.alert.alert-danger").length).toEqual(0);
    });

    it("Method info() sets alert-info style class.", function() {
        $qAlert.info("the-message");

        expect($body.find("div.status-panel.alert.alert-success").length).toEqual(0);
        expect($body.find("div.status-panel.alert.alert-info").length).toEqual(1);
        expect($body.find("div.status-panel.alert.alert-warning").length).toEqual(0);
        expect($body.find("div.status-panel.alert.alert-danger").length).toEqual(0);
    });

    it("Method warning() sets alert-warning style class.", function() {
        $qAlert.warning("the-message");

        expect($body.find("div.status-panel.alert.alert-success").length).toEqual(0);
        expect($body.find("div.status-panel.alert.alert-info").length).toEqual(0);
        expect($body.find("div.status-panel.alert.alert-warning").length).toEqual(1);
        expect($body.find("div.status-panel.alert.alert-danger").length).toEqual(0);
    });

    it("Method danger() sets alert-danger style class.", function() {
        $qAlert.danger("the-message");

        expect($body.find("div.status-panel.alert.alert-success").length).toEqual(0);
        expect($body.find("div.status-panel.alert.alert-info").length).toEqual(0);
        expect($body.find("div.status-panel.alert.alert-warning").length).toEqual(0);
        expect($body.find("div.status-panel.alert.alert-danger").length).toEqual(1);
    });

    it("Set parameterized message.", function() {
        $qAlert.danger("the-message {0} and {1}.", "AAAA", "BBBB");

        expect($body.find("span.alert-body").text()).toMatch("the-message AAAA and BBBB.");
    });

    it("Set parameterized message with last number.", function() {
        $qAlert.danger("the-message {0} and {1}.", "AAAA", 500);

        expect($body.find("span.alert-body").text()).toMatch("the-message AAAA and 500.");
    });

    it("Alert closes by 10 seconds timeout.", function() {
        $qAlert.success("the-message");

        testTimeout(10000);
    });

    it("Setting timeout for alert and without message parameters.", function() {
        $qAlert.success("the-message", 5000);

        testTimeout(5000);
    });

    it("Setting timeout for alert with message parameters.", function() {
        $qAlert.success("the-message {0} and {1}.", "AAA", "BBB", 5000);

        expect($body.find("span.alert-body").text()).toMatch("the-message AAA and BBB.");
        testTimeout(5000);
    });

    it("Setting timeout for alert with message parameters and last not number.", function() {
        $qAlert.success("the-message {0} and {1}.", "AAA", "BBB", "XXXX");

        expect($body.find("span.alert-body").text()).toMatch("the-message AAA and BBB.");
        testTimeout(10000)
    });

    function testTimeout(value) {
        expect($.fn.alert).not.toHaveBeenCalledWith('close');

        $timeout.flush(value-1);
        expect($.fn.alert).not.toHaveBeenCalledWith('close');

        $timeout.flush(1);
        expect($.fn.alert).toHaveBeenCalledWith('close');
    }

});