jQuery(function () {
    $(document).ready(function () {
        if ($("#action").val() == "visualizando") {
            $('input:not([type=image],[type=button],[type=submit],[type=hidden])').each(disable);
            $('select').each(disable);
        }
    });

    function disable() {
        $(this).attr("disabled", "true")
    }
});