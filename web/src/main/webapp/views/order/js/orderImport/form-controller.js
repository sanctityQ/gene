
define(function(require, exports, module) {
  var hotCtrler = require('./hot-controller');
  var plateCtrler = require('./plate-controller');

  var formCtrler = {
    init: function() {
      this.$createRow = $('#J-createRow');
      this.bindEvent();
    },
    bindEvent: function() {
      this.$createRow.on('click', function() {
        var amount = $('#J-rowAmount').val() || 0;
        hotCtrler.insert(amount);
        plateCtrler.setupPlateChoice(amount);
      });

      $('#J-rowAmount').on('focus', function() {
        $(this).removeClass('input-invalid');
      }).on('blur', function() {
        var val = $(this).val();

        if (isNaN(val)) {
          $(this).addClass('input-invalid');
        }
      })
    }
  };

  module.exports = formCtrler;
});