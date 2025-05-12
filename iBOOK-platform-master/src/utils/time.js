import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
import relativeTime from 'dayjs/plugin/relativeTime';

dayjs.extend(relativeTime);

export default function renderTime(time) {
  const formatted = dayjs(time).locale('zh-cn').fromNow();
  return formatted;
}

export function renderBeginTime(time) {
  console.log('formatted');
  let nowTime = new Date();
  if (nowTime.getDate() == dayjs(time).getDate()) {
    const formatted = dayjs(time).getTime();
  } else {
    const formatted = dayjs(time).locale('zh-cn').fromNow();
  }
  console.log(formatted);
  return formatted;
}
