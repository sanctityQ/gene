
define(function(require, exports, module) {

  var platectrler = {
    init: function() {
      this.$switchMethod = $('#J-switchMethod');
      this.$switchDirection = $('#J-switchDirection');
      this.$form = $('#J-plateForm');
      this.$platePreview = $('#J-platePreview');
      this.$imagePreview = $('#J-imagePreview');
      this.bindEvent();
    },
    bindEvent: function() {
      var self = this;
      //切换提交方式
      this.$switchMethod.on('click', function() {
        var method = $(this).data('method');
        method === 'tube' ? self.plateHandler(this, self) : self.tubeHandler(this, self);
      });
      //切换tube提交方向
      this.$switchDirection.on('click', function() {
        var direction = $(this).data('direction');
        //1横向、2纵向
        direction === 1 ? self.verticalDirectionHandler(this, self) : self.horizontalDirectionHandler(this, self);
      });
    },
    //横向提交
    horizontalDirectionHandler: function(ele, ctx) {
      $(ele).data('direction', 1);
      $(ele).find('.placeholder-text').text(SNGR_CONTAINER_LAYOUT_VERTICAL);
      ctx.$platePreview.find('>.tb-header').addClass('highlight');
      ctx.$platePreview.find('>.tb-body').removeClass('highlight');
    },
    //纵向提交
    verticalDirectionHandler: function(ele, ctx) {
      $(ele).data('direction', 2);
      $(ele).find('.placeholder-text').text(SNGR_LABELS_HORIZONTAL);
      ctx.$platePreview.find('>.tb-header').removeClass('highlight');
      ctx.$platePreview.find('>.tb-body').addClass('highlight');
    },
    //按管提交方式
    tubeHandler: function(ele, ctx) {
      $(ele).data('method', 'tube');
      $(ele).find('>span:last').text(ToPlate);
      ctx.$platePreview.hide();
      ctx.$imagePreview.show();
      this.$form.hide();
    },
    //按板提交方式
    plateHandler: function(ele, ctx) {
      $(ele).data('method', 'plate');
      $(ele).find('>span:last').text(ToTube);
      ctx.$imagePreview.hide();
      ctx.$platePreview.show();
      this.$form.show();
    }
  };

  module.exports = platectrler;
});