db = connect( 'mongodb://yoda:starwars@localhost:27018/rdbms?directConnection=true&authSource=admin' );

db.customers.createIndex({'firstName': 1, 'lastName': 1, 'address.city':1})
db.orders.createIndex({'orderDate': 1, 'orderStatus': 1, 'customer.lastName': 1})
db.orders.createIndex({'shippingMethod': 1, 'orderStatus': 1, 'shippingStatus': 1})

db.orders.createSearchIndex("default", {mappings: {dynamic: true} })
db.customers.createSearchIndex("customer_search", {mappings: {dynamic: true}})

db.products.createSearchIndex("default", {
    mappings: {
        dynamic: true,
        fields: {
            name: { type: "autocomplete",
                    minGrams: 3
            }
        }
    }
})

db.stores.createSearchIndex("default", {
    mappings: {
        dynamic: true,
        fields: {
            name: { type: "autocomplete",
                minGrams: 3
            }
        }
    }
})

db.orders.aggregate(
    [
    {
        $group: {
            _id: "$customer._id",
            orders: {
                $addToSet: "$$ROOT"
            }
        }
    },
    {
        $project: {
            _id: 1,
            recentOrders: {
                $slice: [
                    {
                        $sortArray: {
                            input: "$orders",
                            sortBy: {
                                $orderDate: -1
                            }
                        }
                    },
                    5
                ]
            }
        }
    },
    {
        $project: {
            "recentOrders.details": 0
        }
    },
    {
        $merge: {
            into: "customers",
            on: "_id",
            whenMatched: "merge"
        }
    }
]
)




