db = connect( 'mongodb://yoda:starwars@localhost:27018/rdbms?directConnection=true&authSource=admin' );

db.customers.createIndex({'firstName': 1, 'lastName': 1, 'address.city':1})
db.orders.createIndex({'orderDate': 1, 'orderStatus': 1, 'customer.lastName': 1})
db.orders.createIndex({'shippingMethod': 1, 'orderStatus': 1, 'shippingStatus': 1})

db.runCommand(
    {
        createSearchIndexes: "orders",
        indexes: [
            {
                name: "default",
                definition: {
                    mappings: {dynamic: true}
                }
            }
        ]
    }
)


