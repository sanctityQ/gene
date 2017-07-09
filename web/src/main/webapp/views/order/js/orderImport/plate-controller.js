
define(function(require, exports, module) {

  var platectrler = {
    proxy: {
      $switchType: $('#J-switchType'),
      $switchDirection: $('#J-switchDirection'),
      $platePreview: $('#J-platePreview')
    },
    init: function() {
      this.$switchType = $('#J-switchType');
      this.$switchDirection = $('#J-switchDirection');
      this.$form = $('#J-plateForm');
      this.$platePreview = $('#J-platePreview');
      this.$imagePreview = $('#J-imagePreview');
      this.$markEmpty = $('#J-markEmpty');
      this.bindEvent();
    },
    bindEvent: function() {
      var self = this;

      //切换提交方式
      this.$switchType.bind('switch:type', function() {
        var type = $(this).data('type');
        type === sngr.containerType.TUBE ? self.plateHandler(this, self) : self.tubeHandler(this, self);
      });

      //切换提交方式
      this.$switchType.bind('click', function() {
        self.$switchType.trigger('switch:type');
        self.$switchType.trigger('changed:type');
      });

      //切换tube提交方向
      this.$switchDirection.bind('click', function() {
        self.$switchDirection.trigger('switch:direction');
        self.$switchDirection.trigger('changed:direction');
      });

      //切换tube提交方向
      this.$switchDirection.on('switch:direction', function() {
        var direction = $(this).data('direction');
        //1横向、2纵向
        direction === sngr.plateDirection.HORIZONTAL ? self.verticalDirectionHandler(this, self) : self.horizontalDirectionHandler(this, self);
      });

      //开启或关闭标记空管
      this.$markEmpty.on('click', function() {
        var isMarking = $(this).data('marking');
        isMarking ? self.markedEmptyHandler(this, self) : self.markingEmptyHandler(this, self);
      });

      //标记空管
      this.$platePreview.on('click', '.tb-body>.tb-row .circle', function() {
        var status = $(this).data('cell-status');
        var isMarking = self.$markEmpty.data('marking');

        //未开启标记空管状态下，点击circle则高亮选择表格中的对应cell
        if (!isMarking) {
          return self.$platePreview.trigger('click:circle', { tag: $(this).data('tag'), index: $(this).parent().index() });
        }

        if (status === 'empty') {
          $(this).removeClass('empty-circle').removeData('cell-status');
        } else {
          $(this).addClass('empty-circle').data('cell-status', 'empty');
        }
      }).on('toggleEditable', '.tb-body>.tb-row .circle', function() {
        $(this).toggleClass('editable-circle');
      })
    },
    //返回按板提交模式的方向横向、纵向
    getDirection: function() {
      return this.$switchDirection.data('direction');
    },
    //是否按板提交模式
    isPlateMode: function() {
      return this.$switchType.data('type') !== sngr.containerType.TUBE;
    },
    //标记空管
    markingEmptyHandler: function(ele, ctx) {
      ctx.$markEmpty.data('marking', true).find('>span').text(MB_UI_APPLY_EMPTY_WELLS);
      ctx.$platePreview.find('.tb-body>.tb-row .circle').toggleClass('editable-circle');
    },
    //确认标记
    markedEmptyHandler: function(ele, ctx) {
      ctx.$markEmpty.data('marking', false).find('>span').text(OLIORDER_MarkEmptyWells);
      ctx.$platePreview.find('.tb-body>.tb-row .circle').toggleClass('editable-circle');
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
      $(ele).data('type', sngr.containerType.TUBE);
      $(ele).find('>span:last').text(ToPlate);
      ctx.$platePreview.hide();
      ctx.$imagePreview.show();
      this.$form.hide();
    },
    //按板提交方式
    plateHandler: function(ele, ctx) {
      $(ele).data('type', sngr.containerType.PLATE);
      $(ele).find('>span:last').text(ToTube);
      ctx.$imagePreview.hide();
      ctx.$platePreview.show();
      this.$form.show();
    }
  };

  module.exports = platectrler;
});