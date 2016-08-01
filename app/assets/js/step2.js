import pikaday from 'pikaday'
import moment from 'moment'
import R from 'ramda'

// const chooseDateView = () => {

  export const startPika = (container, croissants) => {
    const picker = new Pikaday({
      disableWeekends: true,
      numberOfMonths: 2,
      minDate: moment().add(1, 'day').toDate(),
      maxDate: moment().add(2, 'month').toDate(),
      disableDayFn: date => {
        console.log(date)
        const isDayTaken = R.find(croissant => {
            if(!croissant.doneDate) return false
            const doneDateMoment = moment(croissant.doneDate)
            console.log('date tested', date)
            console.log('doneDateMoment', doneDateMoment)
            console.log(moment(date).isSame(doneDateMoment, 'd'))
            return moment(date).isSame(doneDateMoment, 'd')
        })(croissants)
        return isDayTaken
      }
    });
    field.parentNode.insertBefore(picker.el, container.nextSibling);
  }
// }
