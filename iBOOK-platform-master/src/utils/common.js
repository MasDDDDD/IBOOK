export function doNothing() {
  console.log('click but Nothing Happened');
}

export var formatDate = function (date) {
  if (date != '' && date != undefined && date != null) {
    date = new Date(date);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? '0' + m : m;
    var d = date.getDate();
    d = d < 10 ? '0' + d : d;
    return y + '-' + m + '-' + d;
  } else {
    return '';
  }
};

export var formatTime = function (date) {
  if (date != '' && date != undefined && date != null) {
    date = new Date(date);
    var h = date.getHours();
    h = h < 10 ? '0' + h : h;
    var m = date.getMinutes();
    m = m < 10 ? '0' + m : m;
    return h + ':' + m;
  } else {
    return '';
  }
};

export var formatFullTime = function (date) {
  if (date != '' && date != undefined && date != null) {
    date = new Date(date);
    var mydate = formatDate(date);
    var mytime = formatTime(date);
    return mydate + ' ' + mytime;
  } else {
    return '';
  }
};
