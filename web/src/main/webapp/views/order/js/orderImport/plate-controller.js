
define(function(require, exports, module) {

  var platectrler = {
    proxy: {
      $switchType: $('#J-switchType'),
      $switchDirection: $('#J-switchDirection'),
      $platePreview: $('#J-platePreview'),
      $plateChoice: $('#J-plateChoice')
    },
    init: function() {
      this.$switchType = $('#J-switchType');
      this.$switchDirection = $('#J-switchDirection');
      this.$form = $('#J-plateForm');
      this.$platePreview = $('#J-platePreview');
      this.$imagePreview = $('#J-imagePreview');
      this.$markEmpty = $('#J-markEmpty');
      this.$plateChoice = $('#J-plateChoice');
      this.$plateInfo = $('#J-plateInfo');
      this.bindEvent();
    },
    createPlateCell: function(range, plateInfo) {
      var self = this;

      this.range = range || this.range;
      this.plateInfo = plateInfo || this.plateInfo || { plates: 1, plateId: 1 };

      var plateCellHtml = Handlebars.compile($('#plate-cell-template').html())(_.extend({}, this.plateInfo, sngr, {
        plateRows: sngr.plateRows.slice(1),
        isHorizontal: self.getDirection() === sngr.plateDirection.HORIZONTAL
      }));

      this.$platePreview.html(plateCellHtml);
      this.$platePreview.find('.tb-body>.tb-row .circle').each(function() {
        var tag = $(this).data('tag');

        if (self.range.indexOf(tag) > -1) {
          $(this).addClass('normal-circle');
        }
      });
    },
    //返回标注为有效孔的范围
    getRange: function(amount, isRevert) {
      var direction = this.getDirection();
      var range = [];

      if (isRevert) {
        this.revert = this.amount;
      }

      this.amount = amount || this.amount;

      if (direction === sngr.plateDirection.HORIZONTAL) {
        var fillRows = ~~(this.amount / 12);
        var rest = this.amount % 12;

        for (var i = 1; i <= fillRows; i++) {
          range.push(sngr.plateCols.map(function(val, index) {
            return sngr.plateRows[i] + ':' + val;
          }));
        }

        range.push(sngr.plateCols.slice(0, rest).map(function(val, index) {
          return sngr.plateRows[fillRows + 1] + ':' + val;
        }));
      }

      if (direction === sngr.plateDirection.VERTICAL) {
        var fillCols = ~~(this.amount / 8);
        var rest = this.amount % 8;

        for (var i = 1; i <= fillCols; i++) {
          range.push(sngr.plateRows.slice(1).map(function(val, index) {
            return val + ':' + i;
          }));
        }

        range.push(sngr.plateRows.slice(1, rest + 1).map(function(val, index) {
          return val + ':' + (fillCols + 1);
        }));
      }

      this.amount = this.revert || this.amount;

      return _.flatten(range);
    },
    //切换板号
    onPlateChoiceChange: function(event) {
      var plateId = $(event.currentTarget).val() || 1;
      this.$plateInfo.text(UI_OligoPlatePreview.replace(/\{0\}/g, plateId).replace(/\{1\}/g, this.plates));

      var amount = (this.amount - (plateId - 1) * 96) / 96 >= 1 ? 96 : (this.amount - (plateId - 1) * 96);
      var range = this.getRange(amount, true);
      this.createPlateCell(range, { plateId: plateId, plates: Math.ceil(this.amount / 96) });

      //非第一板时，禁用切换功能
      this.$switchType.toggleClass(function() {return 'disabled'}, +plateId !== 1);
      this.$switchDirection.toggleClass(function() {return 'disabled'}, +plateId !== 1);

      return {
        range: range,
        plateId: plateId
      }
    },
    //创建板号选择
    setupPlateChoice: function(amount) {
      var $plateChoice = this.$plateChoice.empty();
      this.plates = Math.ceil(amount / 96) || 1;

      for (var i = 1; i <= this.plates; i++) {
        var option = $('<option>').val(i).text(i);
        $plateChoice.append(option);
      }

      this.$plateInfo.text(UI_OligoPlatePreview.replace(/\{0\}/g, 1).replace(/\{1\}/g, this.plates));

      var range = this.getRange(amount);
      this.createPlateCell(range, {
        plates: this.plates,
        plateId: 1
      });
    },
    bindEvent: function() {
      var self = this;

      //切换板
      this.$plateChoice.bind('change', function(event) {
        var info = self.onPlateChoiceChange(event);
        self.$plateChoice.trigger('change:choice', info);
      });

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
        self.createPlateCell(self.getRange());
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