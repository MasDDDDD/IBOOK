<template>
  <v-row class="fill-height">
    <v-col>
      <v-sheet height="64">
        <v-toolbar
          flat
        >
          <v-btn
            outlined
            class="mr-4"
            color="grey darken-2"
            @click="setToday"
          >
            Today
          </v-btn>
          <v-btn
            fab
            text
            small
            color="grey darken-2"
            @click="prev"
          >
            <v-icon small>
              mdi-chevron-left
            </v-icon>
          </v-btn>
          <v-btn
            fab
            text
            small
            color="grey darken-2"
            @click="next"
          >
            <v-icon small>
              mdi-chevron-right
            </v-icon>
          </v-btn>
          <v-toolbar-title v-if="$refs.calendar">
            {{ $refs.calendar.title }}
          </v-toolbar-title>
          <v-spacer />
        </v-toolbar>
      </v-sheet>
      <v-sheet height="600">
        <v-calendar
          ref="calendar"
          v-model="focus"
          color="primary"
          type="category"
          :first-interval="st"
          :interval-count="ct"
          category-show-all
          :categories="categories"
          :events="events"
          :event-color="getEventColor"
          :event-ripple="false"
          @change="getEvents"
          @mousedown:event="startDrag"
          @mousedown:time-category="startTime"
          @mousemove:time-category="mouseMove"
          @mouseup:time-category="endDrag"
          @mouseleave="cancelDrag"
        />
      </v-sheet>
    </v-col>
    <v-dialog v-model="showDialog" persistent max-width="500">
      <v-card>
        <v-card-title class="headline">
          取消预定？
        </v-card-title>

        <v-card-actions>
          <v-spacer />
          <v-btn color="green darken-1" text @click="showDialog = false">
            再想想
          </v-btn>
          <v-btn color="green darken-1" text @click="delThisEvent">
            确认
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="showDialog2" persistent max-width="500">
      <v-card>
        <v-card-title class="headline">
          时间冲突
        </v-card-title>

        <v-card-actions>
          <v-spacer />
          <v-btn color="green darken-1" text @click="showDialog2 = false">
            OK
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="showDialog3" persistent max-width="500">
      <v-card>
        <v-card-title class="headline">
          预定成功
        </v-card-title>

        <v-card-actions>
          <v-spacer />
          <v-btn color="green darken-1" text @click="showDialog3 = false">
            OK
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="showDialog4" persistent max-width="500">
      <v-card>
        <v-card-title class="headline">
          不在可预定时间段，预定失败
        </v-card-title>

        <v-card-actions>
          <v-spacer />
          <v-btn color="green darken-1" text @click="showDialog4 = false">
            OK
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="showDialog5" persistent max-width="500">
      <v-card>
        <v-card-title class="headline">
          有座位没有签到，无法预约
        </v-card-title>

        <v-card-actions>
          <v-spacer />
          <v-btn color="green darken-1" text @click="showDialog5 = false">
            OK
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="showDialog6" persistent max-width="500">
      <v-card>
        <v-card-title class="headline">
          预约时间超过限制，请重新预约
        </v-card-title>

        <v-card-actions>
          <v-spacer />
          <v-btn color="green darken-1" text @click="showDialog6 = false">
            OK
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>
<script>
import { mapGetters, mapState } from 'vuex';
import { getRoom, getSeats, getBooks, booking } from '../../services/booking.service';
import log from '../../utils/log';
import { formatDate, formatFullTime } from '../../utils/common';

export default {
  data: () => ({
    focus: '',
    showDialog: false,
    showDialog2: false,
    showDialog3: false,
    showDialog4: false,
    showDialog5: false,
    showDialog6: false,
    value: '',
    events: [],
    event: null,
    colors: ['#2196F3', '#3F51B5', '#673AB7', '#00BCD4', '#4CAF50', '#FF9800', '#757575'],
    names: ['Meeting', 'Holiday', 'PTO', 'Travel', 'Event', 'Birthday', 'Conference', 'Party'],
    categories: [],
    dragEvent: null,
    dragStart: null,
    createEvent: null,
    createStart: null,
    extendOriginal: null,
    bookedToday: {},
    st: null,
    ct: null,
  }),
  computed: {
    ...mapState(['user', 'seats', 'books', 'room']),
  },
  mounted() {
    this.$refs.calendar.checkChange();
  },
  created() {
    this.$store.commit('SET_BAR_TITLE', '预约');
    this.fetchData();
  },
  methods: {
    fetchData() {
      getRoom(this.$route.params.id).then((room) => {
        if (room.state === 0) {
          this.st = 0;
          this.ct = 0;
        } else {
          this.st = room.startTime;
          this.ct = room.endTime - room.startTime;
        }
      });
      getSeats(this.$route.params.id)
        .then((seats) => {
          this.$store.commit('SET_SEATS', seats);
          let ps = [];
          this.seats.forEach((seat) => {
            this.categories.push(`座位${seat.id}`);
            ps.push(getBooks(seat.id));
          });
          Promise.all(ps).then((books) => {
            this.$store.commit('SET_BOOKS', books);
            this.getEvents({
              start: { date: formatDate(new Date()) },
              end: { date: '2' },
            });
          });
        })
        .catch((err) => {
          log.info(err);
        });
    },
    setToday() {
      this.focus = '';
    },
    prev() {
      this.$refs.calendar.prev();
    },
    next() {
      this.$refs.calendar.next();
    },
    clickEvent({ event }) {
      if (event.name === '我') this.showDialog = true;
      this.event = event;
    },
    delEvent(event) {
      const i = this.events.indexOf(event);
      if (i !== -1) {
        this.events.splice(i, 1);
      }
    },
    delThisEvent() {
      const i = this.events.indexOf(this.event);
      if (i !== -1) {
        const j = this.bookedToday[this.event.category].indexOf([this.event.start, this.event.end]);
        this.bookedToday[this.event.category].splice(j, 1);
        this.events.splice(i, 1);
      }
      this.showDialog = false;
    },
    startDrag({ event, timed }) {
      if (event && timed) {
        this.dragEvent = event;
        this.dragTime = null;
        this.extendOriginal = null;
      }
    },
    startTime(tms) {
      const mouse = this.toTime(tms);

      if (this.dragEvent && this.dragTime === null) {
        const start = this.dragEvent.start;

        this.dragTime = mouse - start;
      } else {
        this.createStart = this.roundTime(mouse);
        this.createEvent = {
          name: `我：待签到`,
          color: this.rndElement(this.colors),
          start: this.createStart,
          end: this.createStart,
          timed: true,
          category: tms.category,
        };

        this.events.push(this.createEvent);
      }
    },
    extendBottom(event) {
      this.createEvent = event;
      this.createStart = event.start;
      this.extendOriginal = event.end;
    },
    mouseMove(tms) {
      const mouse = this.toTime(tms);

      if (this.createEvent && this.createStart !== null) {
        const mouseRounded = this.roundTime(mouse, false);
        const min = Math.min(mouseRounded, this.createStart);
        const max = Math.max(mouseRounded, this.createStart);

        this.createEvent.start = min;
        this.createEvent.end = max;
      }
    },
    endDrag() {
      const ce = this.createEvent;
      if (this.createEvent) {
        if (this.isConflict(this.createEvent)) {
          this.showDialog2 = true;
          this.delEvent(this.createEvent);
        } else {
          const seatId = this.createEvent.category.substring(2);
          const startTime = formatFullTime(new Date(this.createEvent.start)) + ':00';
          const bookingPeriod = (this.createEvent.end - this.createEvent.start) / (60 * 60 * 1000);
          booking(seatId, startTime, bookingPeriod).then(
            (res) => {
              this.showDialog3 = true;
              if (!this.bookedToday[this.createEvent.category])
                this.bookedToday[this.createEvent.category] = [];
              this.bookedToday[this.createEvent.category].push([
                this.createEvent.start,
                this.createEvent.end,
              ]);
            },
            (err) => {
              if (err.response.data.message === "you've not signed in your current seat!")
                this.showDialog5 = true;
              else if (err.response.data.message === 'booking period cannot be larger than 4h!')
                this.showDialog6 = true;
              else this.showDialog4 = true;
              this.delEvent(ce);
            }
          );
        }
      }
      this.dragTime = null;
      this.dragEvent = null;
      this.createEvent = null;
      this.createStart = null;
      this.extendOriginal = null;
    },
    cancelDrag() {
      if (this.createEvent) {
        if (this.extendOriginal) {
          this.createEvent.end = this.extendOriginal;
        } else {
          const i = this.events.indexOf(this.createEvent);
          if (i !== -1) {
            this.events.splice(i, 1);
          }
        }
      }

      this.createEvent = null;
      this.createStart = null;
      this.dragTime = null;
      this.dragEvent = null;
    },
    roundTime(time, down = true) {
      const roundTo = 60; // minutes
      const roundDownTime = roundTo * 60 * 1000;

      return down ? time - (time % roundDownTime) : time + (roundDownTime - (time % roundDownTime));
    },
    toTime(tms) {
      return new Date(tms.year, tms.month - 1, tms.day, tms.hour, 0).getTime();
    },
    getEventColor(event) {
      const rgb = parseInt(event.color.substring(1), 16);
      const r = (rgb >> 16) & 0xff;
      const g = (rgb >> 8) & 0xff;
      const b = (rgb >> 0) & 0xff;

      return event === this.dragEvent
        ? `rgba(${r}, ${g}, ${b}, 0.7)`
        : event === this.createEvent
        ? `rgba(${r}, ${g}, ${b}, 0.7)`
        : event.color;
    },
    getEvents({ start, end }) {
      log.info(this.books);
      const events = [];
      this.bookedToday = {};

      for (let i = 0; i < this.books.length; i++) {
        if (this.books[i].length) {
          const category = '座位' + String(this.books[i][0].seatId);
          const bookedTime = [];
          this.books[i].forEach((book) => {
            const day = book.startTime.split(' ')[0];
            const state = book.state;
            const time = book.startTime.split(' ')[1];
            const startTime = new Date(`${day}T${time}`).getTime();
            const endTime = startTime + book.bookingPeriod * 60 * 60 * 1000;
            // log.info(day, start.date);
            if (day === start.date && (state === -1 || state === 0 || state === 1)) {
              // log.info(start);
              let s = '';
              if (state === -1) s = '抢座待签到';
              else if (state === 0) s = '待签到';
              else s = '使用中';
              events.push({
                name: book.stuNum === this.user.stuNum ? '我：' + s : book.stuNum,
                color: this.colors[Number(book.id) % this.colors.length],
                start: startTime,
                end: endTime,
                timed: true,
                category,
              });
              bookedTime.push([startTime, endTime]);
            }
          });
          this.bookedToday[category] = bookedTime;
          // log.info(this.bookedToday);
        }

        // events.push({
        //   name: this.rndElement(this.names),
        //   color: this.rndElement(this.colors),
        //   start,
        //   end,
        //   timed,
        // });
      }

      this.events = events;
    },
    rnd(a, b) {
      return Math.floor((b - a + 1) * Math.random()) + a;
    },
    rndElement(arr) {
      return arr[this.rnd(0, arr.length - 1)];
    },
    isConflict(event) {
      // 是否时间有冲突
      const start = event.start;
      const end = event.end;
      let flag = false;
      if (typeof this.bookedToday[event.category] === typeof []) {
        this.bookedToday[event.category].forEach((data) => {
          if (
            (end > data[0] && end <= data[1]) ||
            (start >= data[0] && start < data[1]) ||
            (start < data[0] && end > data[1])
          ) {
            flag = true;
          }
        });
      }
      return flag;
    },
  },
};
</script>
<style scoped lang="scss">
.v-event-draggable {
  padding-left: 6px;
}

.v-event-timed {
  user-select: none;
  -webkit-user-select: none;
}

.v-event-drag-bottom {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 4px;
  height: 4px;
  cursor: ns-resize;

  &::after {
    display: none;
    position: absolute;
    left: 50%;
    height: 4px;
    border-top: 1px solid white;
    border-bottom: 1px solid white;
    width: 16px;
    margin-left: -8px;
    opacity: 0.8;
    content: '';
  }

  &:hover::after {
    display: block;
  }
}
</style>
