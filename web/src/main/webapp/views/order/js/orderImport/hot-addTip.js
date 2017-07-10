
define(function(require, exports, module) {
  module.exports = function() {
    $('.easyui-tooltip').tooltip({
      position: 'right',
      onShow: function(){
        $(this).tooltip('tip').css({
          width: 375
        });
      }
    });
  }
});