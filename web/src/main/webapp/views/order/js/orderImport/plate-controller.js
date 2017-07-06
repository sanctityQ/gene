
define(function(require, exports, module) {

  var platectrler = {
    init: function() {
      this.$switch = $('#J-switchMethod');
      this.$form = $('#J-plateForm');
      this.bindEvent();
    },
    bindEvent: function() {
      var self = this;
      this.$switch.on('click', function() {
        var method = $(this).data('method');
        method === 'tube' ? self.plateHandler(this) : self.tubeHandler(this);
      })
    },
    //按管提交方式
    tubeHandler: function(ele) {
      $(ele).data('method', 'tube');
      $(ele).find('>span:last').text(ToPlate);
      $('#J-platePreview').hide();
      $('#J-imagePreview').show();
      this.$form.hide();
    },
    //按板提交方式
    plateHandler: function(ele) {
      $(ele).data('method', 'plate');
      $(ele).find('>span:last').text(ToTube);
      $('#J-imagePreview').hide();
      $('#J-platePreview').show();
      this.$form.show();
    }
  };

  module.exports = platectrler;
});