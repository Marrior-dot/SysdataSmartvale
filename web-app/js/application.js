// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better 
// to create separate JavaScript files as needed.
//
//= require plugins/jquery-1.11.3.min.js

//= require jquery.validator/jquery.validate.min.js
//= require jquery.validator/additional-methods.min.js
//= require jquery.validator/message_pt-BR.js
//= require jquery.validator/init.js

//= require angular.js
//= require web-anp/app.js
//= require_tree plugins
//= require_tree web-anp/services
//= require_tree web-anp/filters
//= require_self
//= require input-list.js
//= require bootstrap-datepicker.js
//= require propriedadesPrograma.js
//= require input.masks.js
var Ajax;
if (Ajax && (Ajax != null)) {
	Ajax.Responders.register({
	  onCreate: function() {
        if($('spinner') && Ajax.activeRequestCount>0)
          Effect.Appear('spinner',{duration:0.5,queue:'end'});
	  },
	  onComplete: function() {
        if($('spinner') && Ajax.activeRequestCount==0)
          Effect.Fade('spinner',{duration:0.5,queue:'end'});
	  }
	});
}
