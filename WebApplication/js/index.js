var app = new Vue({
   el: '#app',
   data: {
        list: [
            {
                id: 0,
                pick: true,
                name: 'Microsoft Lumia 9102',
                price: 8888,
                count: 1
            },
            {
                id: 1,
                pick: true,
                name: 'Microsoft Surface Pro 6',
                price: 6666,
                count: 1
            },
            {
                id: 2,
                pick: false,
                name: 'Apple iPad Pro 13`',
                price: 5555,
                count: 1
            }
        ]
   },
    computed: {
        total: function () {
            var total = 0;
            for (var i = 0; i < this.list.length; ++i) {
                var item = this.list[i];
                if(item.pick)  {
                    total += item.price * item.count;
                }
            }
            return total.toString().replace(/\B(?=(\d{3})+$)/g, ',');
        }
    },
    methods: {
        handleReduce: function (index) {
            if (this.list[index].count === 0) {
            } else {
                this.list[index].count--;
            }
        },
        handleAdd: function (index) {
            this.list[index].count++;
        },
        handleRemove: function (index) {
            this.list.splice(index, 1);
        },
        handleCheckbox: function (index) {
            this.list[index].pick = !this.list[index].pick;
        }
    }
});