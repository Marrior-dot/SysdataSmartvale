/**
 * Created by andrecunha on 17/08/15.
 */

$(function() {
    $('#geral').change(function() {
        var value = $('#geral').prop('checked');
        $('div.list tr td input:checkbox').prop('checked', value);
    });
});