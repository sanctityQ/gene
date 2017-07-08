
define(function(require, exports, module) {
  module.exports = function(hot) {
    hot.updateSettings({
      // colWidths: [50, 50, 60, 100, 420, 60, 60, 60, 140, 120, 1],
      // colWidths: [1, 1, 40, 90, 338, 60, 60, 70, 80, 140, 140, 190, 1],
      // colWidths: [40, 40, 40, 90, 270, 50, 60, 70, 80, 140, 140, 190, 1],
      fixedColumnsLeft: 3,
      fillHandle: "vertical",
      height: 300,
      hiddenColumns: {},
      contextMenu: {
        callback: function (key, options) {
          if (key === 'about') {
            setTimeout(function () {
              // timeout is used to make sure the menu collapsed before alert is shown
              alert('This is a context menu with default and custom options mixed');
            }, 100);
          }
        },
        items: {
          'row_above': {
            name: DataPageInfo.HandsonTableInfo.UI_OligoInsertAbove,
            disabled: function () {
              if (hot.getSelected() == undefined)
                return true;
              var n = hot.getSelected()[0]
                , t = hot.getSourceData();
              return n === 0 || t.length === 96 ? true : t[n - 1] !== undefined
              && t[n - 1] !== null && t[n - 1].Status === 5 ? true : void 0
            }
          },
          'row_below': {
            name: DataPageInfo.HandsonTableInfo.UI_OligoInsertBelow,
            disabled: function() {
              if (hot.getSelected() == undefined)
                return true;
              var n = hot.getSelected()[0]
                , t = hot.getSourceData();
              return n === 96 || t.length === 96 ? true : t[n + 1] !== undefined
              && t[n + 1] !== null && t[n + 1].Status === 5 ? true : void 0
            }
          },
          'remove_row': {
            name: DataPageInfo.HandsonTableInfo.UI_OligoRemoverow,
            disabled: function() {
              if (hot.getSelected() == undefined)
                return true;
              var n = hot.getSelected()[0]
                , t = hot.getSourceData();
              if (t[n] !== undefined && t[n] !== null && t[n].Status === 5)
                return true
            }
          },
          'add': {
            name: DataPageInfo.HandsonTableInfo.UI_OligoAddToLibrary,
            disabled: function() {
              if (hot.getSelected() == undefined || hot.getSelected()[1] !== 4
                && hot.getSelected()[1] !== 3 || hot.getSelected()[3] != 4
                && hot.getSelected()[3] != 3)
                return true;
              var n = hot.getSelected()[0]
                , t = hot.getSourceData();
              if (t[n] !== undefined && t[n] !== null && t[n].Status === 5)
                return true
            }
          },
          'select': {
            name: DataPageInfo.HandsonTableInfo.UI_OligoSelectFromLibrary,
            disabled: function() {
              if (hot.getSelected() == undefined || hot.getSelected()[1] !== 4
                && hot.getSelected()[1] !== 3 || hot.getSelected()[3] != 4
                && hot.getSelected()[3] != 3)
                return true;
              var n = hot.getSelected()[0]
                , t = hot.getSourceData();
              if (t[n] !== undefined && t[n] !== null && t[n].Status === 5)
                return true
            }
          }
        }
      }
    })
  }
});