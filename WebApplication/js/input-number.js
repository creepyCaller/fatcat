Vue.component('input-number', {
    template: '\
        <div class="input-number">\
            <div class="input-group>" \
                <input\
                    type="text"\
                    class="form-control"\
                    :value="currentValue"\
                    @change="handleChange">\
                <div class="input-group-btn">\
                    <button\
                        class="btn btn-outline-secondary"\
                        @click="handleDown"\
                        :disabled="currentValue <= min">-</button>\
                    <button\
                        class="btn btn-outline-primary"\
                        @click="handleUp"\
                        :disabled="currentValue >= max">+</button>\
                </div>\
            </div>\
        </div>',
    props: {
        max: {
            type: Number,
            default: Infinity
        },
        min: {
            type: Number,
            default: -Infinity
        },
        value: {
            type: Number,
            default: 0
        }
    },
    data: function () {
        return {
            currentValue: this.value
        }
    },
    watch: {
        currentValue: function (val) {
            this.$emit('input', val);
            this.$emit('on-change', val);
        },
        value: function (val) {
            this.updateValue(val);
        }
    },
    methods: {
        updateValue: function (val) {
            if (val > this.max) {
                val = this.max;
            }
            if (val < this.min) {
                val = this.min;
            }
            this.currentValue = val;
        },
        handleChange: function (event) {
            var val = event.target.value.trim();

            var max = this.max;
            var min = this.min;
            //if is number
                val = Number(val);
                this.currentValue = val;

                if (val > max) {
                    this.currentValue = max;
                } else if (val < min) {
                    this.currentValue = min;
                }
            //else

        },
        handleDown: function () {
            if (this.currentValue <= this.min) return;
            this.currentValue -= 1;
        },
        handleUp: function () {
            if (this.currentValue >= this.max) return;
            this.currentValue += 1;
        }
    },
    mounted: function () {
        this.updateValue(this.value);
    }
});