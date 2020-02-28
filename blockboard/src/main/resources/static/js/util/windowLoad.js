/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file windowLoad.js
 */
let alarmIntervalPeriod = 1000 * 10;

window.onload = function () {
  getBoardList(updateTab);
  getAlarms(1);
  getUnreadAlarmCount();
  setInterval(function () {
    alarmUpdateInterval();
  }, alarmIntervalPeriod);
  resetFunctionAble();
};