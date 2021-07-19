// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require jquery-2.2.0.min
//= require bootstrap
//= require sb-admin-2
//= require mentisMenu.min
//= require dataTable/jquery.dataTables.min
//= require mascaras
//= require bootstrap-datepicker
//= require bootstrap-waitingfor
//= require jquery.mask.min
//= require jquery.maskMoney.min
//= require plugins/bootstrap-select
//= require_tree .
//= require_self



/*
<script type="text/javascript" src="${resource(dir:'js',file:'chart-config.js') }"></script>
<script type="text/javascript" src="${resource(dir:'js',file:'plugins/Chart.min.js') }"></script>

*/



if (typeof jQuery !== 'undefined') {
    (function($) {
        $(document).ajaxStart(function() {
            $('#spinner').fadeIn();
        }).ajaxStop(function() {
            $('#spinner').fadeOut();
        });
    })(jQuery);
}
