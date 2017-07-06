
define(function(require, exports, module) {
  module.exports = {
    emptyRowRenderer: function(n, t, i, r, u, f) {
      Handsontable.renderers.TextRenderer.apply(this, arguments);
      f == 5 && $(t).parent().children("td").css("background-color", "#c0c0c0");
    },
    yellowRenderer: function(n, t) {
      Handsontable.renderers.TextRenderer.apply(this, arguments);
      t.style.backgroundColor = "yellow";
    }
  }
});