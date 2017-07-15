
define(function(require, exports, module) {
  var hot = require('./hot-init');
  var renderer = require('./hot-renderer');

  var toCheckUniqueForPrimerName = {};
  var toCheckUniqueForSequence = {};

  var getSamplesByPlateId = function(n, t) {
    return _.filter(n, function(n) {
      return n.CustPlateID == t
    })
  };

  var getSamplesByName = function(n, t) {
    return _.filter(n, function(n) {
      return n.PrimerName == t
    })
  };

  var getSamplesBySeq = function(n, t) {
    return _.filter(n, function(n) {
      return n.Sequence == t
    })
  };

  //引物名称与序列唯一性检测
  var getUniqueForCheck = function(dataList) {
    //重置
    toCheckUniqueForPrimerName = {};
    toCheckUniqueForSequence = {};

    $.each(dataList, function(index) {
      var item = dataList[index];
      if (!dataList[index].isEmptyRow) {
        var primerName = item.PrimerName || '';
        var sequence = item.Sequence ? '' : item.Sequence.toUpperCase();

        var checkPrimerNameval = toCheckUniqueForPrimerName[primerName];
        var checkSequenceval = toCheckUniqueForSequence[sequence];

        if (primerName.length === 0 || !checkPrimerNameval) {
          toCheckUniqueForPrimerName[primerName] = index + 1;
        } else {
          toCheckUniqueForPrimerName[primerName] = [checkPrimerNameval,  index + 1].join();
        }

        if (sequence.length === 0 || !checkSequenceval) {
          toCheckUniqueForSequence[sequence] = index + 1;;
        } else {
          toCheckUniqueForSequence[sequence] = [checkSequenceval,  index + 1].join();
        }
      }
    });
  };

  //**引物名称***//
  var primerNameValidator = function(primerName, index, cell, updater) {
    var maxPrimeNameLen = 30;
    var col = 3;

    //不为空
    if (primerName.length === 0) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_PrimerName;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment }
      });
    }

    //验证不超过最大长度
    if (primerName.length > maxPrimeNameLen) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_PrimerNameLength;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment.replace('{0}', maxPrimeNameLen) }
      });
    }

    //引物名称唯一性判断
    var result = toCheckUniqueForPrimerName[primerName].toString();
    if (result.indexOf(',') > -1) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_SameName;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment.replace('{0}', '').replace('{1}', result) }
      });
    }
  };

  //引物名称与序列联合检测，
  //相同的序列对应相同的名称
  //相同的名称对应相同的序列
  var primerCombineSeqValidator = function(primerName, sequence, index, updater, dataList) {
    var resultByName = getSamplesByName(dataList, primerName);
    var resultBySeq = getSamplesBySeq(resultByName, sequence);

    var resultBySeq2nd = getSamplesBySeq(dataList, sequence);
    var resultByName2nd = getSamplesByName(resultBySeq2nd, primerName);

    var col = 3;

    if (resultByName.length !== resultBySeq.length) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLISNameSeq;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: {value: comment}
      }, {
        row: index,
        col: col + 1,
        renderer: renderer.yellowRenderer,
        comment: {value: comment}
      });
    }

    if (resultBySeq2nd.length !== resultByName2nd.length) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLISNameSeq;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment }
      }, {
        row: index,
        col: col + 1,
        renderer: renderer.yellowRenderer,
        comment: { value: comment }
      });
    }
  };

  //**序列***//
  var seqValidator = function(sequence, index, cell, updater) {
    var maxSequenceLen = 250;
    var minSequenceLen = 5;
    var col = 4;

    //不为空
    if (sequence.length === 0) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_Blank;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment }
      });
    }

    //校验序列的最小长度
    if (sequence.length < minSequenceLen) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_SeqMinLen;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment.replace('{0}', minSequenceLen) }
      });
    }

    //验证不超过最大长度
    if (sequence.length > maxSequenceLen) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_SequenceLength;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment.replace('{0}', maxSequenceLen) }
      });
    }

    //非法字符校验
    var invalidResult = sequence.replace(/[ATGCMRWSYKVHDBN]/ig, '');
    if (invalidResult.length) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_InvalidCharacter;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment.replace('{0}', invalidResult) }
      });
    }
  };

  //**需求量***//
  var quantityValidator = function(quantity, index, cell, updater) {
    var maxForODTube = 999999999;
    var col = 6;
    var synthesisScaleUS = ['25', '50'];
    var synthesisScale = ['25', '50', '100'];

    //长度限制
    if (+quantity > maxForODTube) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLI_ODTubeLimit;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment.replace('{0}', maxForODTube) }
      });
    }

    //需求量必须为正整数
    if (!quantity || !(+quantity > 0)) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_SynthesisScale;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment }
      });
    }

    //todo: 具体规则不明确
    // if ($.inArray(quantity, synthesisScaleUS) === -1) {
    //   comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Scale_In;
    //   updater.push({
    //     row: index,
    //     col: col,
    //     renderer: renderer.yellowRenderer,
    //     comment: { value: comment }
    //   });
    // }
  };

  //**分管总数***//
  var tubeValidator = function(tubes, quantity, index, cell, updater) {
    var maxForODTube = 999999999;
    var col = 7;
    var isNmole = +$('input:radio[name="IsNmole"]:checked').val() === 1;

    //长度限制
    if (+tubes > maxForODTube) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLI_ODTubeLimit;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment.replace('{0}', maxForODTube) }
      });
    }

    //分管总数必须为正整数
    if (!tubes || !(+tubes > 0)) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_TubeQty;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment }
      });
    }

    //2nmol及以下的需求量，无法分装
    if (isNmole) {
      if ((!quantity || +quantity <= 2) && tubes > 1) {
        comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_NomolTube;
        updater.push({
          row: index,
          col: col,
          renderer: renderer.yellowRenderer,
          comment: { value: comment }
        });
      }
    }
  };

  //**纯化方式***//
  var purityTypeValidator = function(purityType, baseNumber, index, cell, updater) {
    var purigyTypeT = ["DSL", "HPLC", "tPAGE", "hPAGE"];
    var purigyTypeH = ["DSL", "HPLC", "hPAGE"];
    var purigyTypeUS = ["Desalted"];
    var col = 8;

    //纯化类型方式校验
    if (baseNumber <= 60) {
      if (!!purityType && $.inArray(purityType, purigyTypeT) === -1) {
        comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Purification_In;
        updater.push({
          row: index,
          col: col,
          renderer: renderer.yellowRenderer,
          comment: { value: comment }
        });
      }
    } else {
      if (!!purityType && $.inArray(purityType, purigyTypeH) === -1) {
        comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Purification_In;
        updater.push({
          row: index,
          col: col,
          renderer: renderer.yellowRenderer,
          comment: { value: comment }
        });
      }
    }
  };

  //**5'修饰***//

  //**3'修饰***//

  //**中间修饰***//


  exports.validationForDraft = function() {
    var cells = hot.getSourceData();
    var maxPrimeNameLen = 30;
    var comment = '';
    var updater = [];

    //唯一性检测
    getUniqueForCheck(cells);

    $.each(cells, function(index, cell) {
      if (!hot.isEmptyRow(cell) && cell.Status != 5) {
        var primerName = cell.PrimerName || '';
        var sequence = cell.Sequence ? cell.Sequence.toUpperCase() : '';
        var baseNumber = cell.Basenumber;
        var quantity = cell.Quantity;
        var tubes = cell.Tubes;
        var purityType = cell.PurityType || '';
        var col = 0;

        primerNameValidator(primerName, index, cell, updater);
        // primerCombineSeqValidator(primerName, sequence, index, updater, cells);
        seqValidator(sequence, index, cell, updater);
        quantityValidator(quantity, index, cell, updater);
        tubeValidator(tubes, quantity, index, cell, updater);
        purityTypeValidator(purityType, baseNumber, index, cell, updater);
      }
    });

    hot.updateSettings({
      cell: updater
    });
  }
});