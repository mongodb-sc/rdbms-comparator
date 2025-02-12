import requests
from mimesis.locales import Locale,LocaleError
import random
from mimesis.schema import Field, Schema, Fieldset
import json
from pymongo import MongoClient
import psycopg2
from psycopg2.extras import execute_values


pgConn = psycopg2.connect("postgres://yoda:starwars@localhost:5433/rdbms")
client = MongoClient('mongodb://yoda:starwars@localhost:27018/?directConnection=true')
mdb = client['rdbms']


customerSQL = 'insert into customers (id, first_name, last_name, title, address_id) values %s'
phonesSQL = 'insert into phone (id, number, type, customer_id) values %s'
emailsSQL = 'insert into email (id, email, type, customer_id) values %s'
addressSQL = 'insert into addresses (id, city, state, street, zip) values %s'
productSQL = 'insert into products (id, name, dept, price, description) values %s'
storeSQL = 'insert into stores (id, name, address_id, manager_name, region, store_type, sq_ft) values %s'
ordersSQL = 'insert into orders (id, order_date,total,store_id,billing_dept,delivery_date,delivery_method,employee_id,fill_date,invoice_date,invoice_id,order_status,order_type,pick_date,purchase_order,shipping_method,shipping_status,total_pieces,warehouse_id,weight,customer_id,address_id) values %s'
detailSQL = 'insert into order_details (id, quantity, order_id, product_id) values %s'

_ = Field(Locale.EN)
_fieldset = Fieldset(Locale.EN)

types = ['work','home','mobile']


def create_customers():
    print('Creating customers...')
    customerSchema = Schema(schema=lambda: {
        "firstName": _("person.first_name"),
        "lastName": _("person.last_name"),
        "title": _("person.title"),
        "address": {
            "city": _("address.city"),
            "country": _("address.country_code"),
            "state": _("address.state", abbr=True),
            "street": _("address.street_number") + ' ' + _("address.street_name"),
            "zip": _("address.zip_code"),
        },
        "phones": _fieldset("text.word", i=2, key=lambda name:{
            "type": random.choice(types),
            "number": _("person.phone_number")
        }),
        "emails": _fieldset( "text.word", i=3, key=lambda name: {
            "type": random.choice(types),
            "email": _("person.email")
        })
        }, iterations=100)


    count = 0
    for x in range(0,100):
        with pgConn.cursor() as cursor:
            addressParams = []
            customerParams = []
            phoneParams = []
            emailParams = []
            customers = customerSchema.create()
            for cust in customers:
                cursor.execute("Select nextval('addresses_seq')")
                addressId = cursor.fetchall()
                cursor.execute("Select nextval('customers_seq')")
                customerId = cursor.fetchall()
                cust['pg_id'] = customerId[0][0]
                cust['address']['pg_id'] = addressId[0][0]
                addressParams.append([addressId[0][0], cust['address']['city'], cust['address']['state'], cust['address']['street'], cust['address']['zip']])
                customerParams.append([customerId[0][0], cust['firstName'], cust['lastName'], cust['title'], addressId[0][0]])
                for phone in cust['phones']:
                    cursor.execute("Select nextval('phone_seq')")
                    phoneId = cursor.fetchall()
                    phoneParams.append([phoneId[0][0], phone['number'], phone['type'], customerId[0][0]])
                for email in cust['emails']:
                    cursor.execute("Select nextval('email_seq')")
                    emailId = cursor.fetchall()
                    emailParams.append([emailId[0][0], email['email'], email['type'], customerId[0][0]])
                count += 1
            execute_values(cursor, addressSQL, addressParams)
            execute_values(cursor, customerSQL, customerParams)
            execute_values(cursor, phonesSQL, phoneParams)
            execute_values(cursor, emailsSQL, emailParams)
            pgConn.commit()
        mdb.customers.insert_many(customers)
    print(f'Created {count} customers')


def create_products():
    print('Creating products')
    productSchema = Schema(schema=lambda: {
        "name": _("choice.choice", items=[ _('food.drink'), _('food.dish'), _('food.fruit')]),
        "dept": random.randint(1,100),
        "price": _("finance.price", minimum=0.01, maximum=10000.00),
        "description":_("text.text", quantity=1)

    }, iterations=100)


    count = 0
    for x in range(0,100):
        with pgConn.cursor() as cursor:
            productParams = []
            products = productSchema.create()
            for product in products:
                cursor.execute("Select nextval('products_seq')")
                productId = cursor.fetchall()
                product['pg_id'] = productId[0][0]
                productParams.append([productId[0][0], product['name'], product['dept'], product['price'], product['description']])
                count += 1
            execute_values(cursor, productSQL, productParams)
            pgConn.commit()
        mdb.products.insert_many(products)
    print(f'Created {count} products')



def create_stores():
    print('Creating Stores')
    storeSchema = Schema(schema=lambda: {
        "name":_('finance.company'),
        "address": {
            "city": _("address.city"),
            "country": _("address.country_code"),
            "state": _("address.state", abbr=True),
            "street": _("address.street_number") + ' ' + _("address.street_name"),
            "zip": _("address.zip_code"),
        },
        "managerName":_('person.full_name'),
        "region":_("choice.choice", items=[ 'Midwest','South','West','North','Great Lakes','Sun Belt','SoCal','NorCal','New England']),
        "storeType":_("choice.choice", items=[ 'Super Store','Standard','Small','Urban','Custom','Embedded']),
        "sqFt": random.randint(1000,10000)
    }, iterations=100)

    count = 0
    for x in range(0,100):
        storeParams = []
        addressParams = []
        stores = storeSchema.create()
        with pgConn.cursor() as cursor:
            for store in stores:
                cursor.execute("Select nextval('addresses_seq')")
                addressId = cursor.fetchall()
                cursor.execute("Select nextval('stores_seq')")
                storeId = cursor.fetchall()
                store['pg_id'] = storeId[0][0]
                addressParams.append([addressId[0][0], store['address']['city'], store['address']['state'], store['address']['street'], store['address']['zip']])
                storeParams.append([storeId[0][0], store['name'], addressId[0][0], store['managerName'], store['region'], store['storeType'], store['sqFt']])
                count+=1
            execute_values(cursor, addressSQL, addressParams)
            execute_values(cursor, storeSQL, storeParams)
            pgConn.commit()
        mdb.stores.insert_many(stores)
    print(f'Created {count} Stores')



def create_orders():
    print('Creating Orders... target is 500k so this might take a few minutes')
    orderSchema = Schema(schema=lambda: {
        "orderDate":_('datetime.datetime', start=2000, end=2024),
        "warehouseId": random.randint(1,9999),
        "fillDate":_('datetime.datetime', start=2000, end=2024),
        "purchaseOrder": _("random.generate_string_by_mask", mask='@@##@@#@@@@##'),
        "invoiceId": random.randint(1,9999),
        "invoiceDate":_('datetime.datetime', start=2000, end=2024),
        "deliveryMethod":_("choice.choice", items=[ 'Door Dash','Uber','Local','Truck','Cargo Ship','Plane','Rail']),
        "weight": random.randint(1,9999),
        "totalPieces": random.randint(1,9999),
        "pickDate":_('datetime.datetime', start=2000, end=2024),
        "shippingMethod":_("choice.choice", items=[ 'Drop','Freight','Carrier','USPS']),
        "billingDept": random.randint(1,9999),
        "orderStatus":_("choice.choice", items=[ 'Received','Accepted','Processed','Filling','Filled','Packed','Shipped','Cancelled']),
        "shippingStatus":_("choice.choice", items=[ 'Ticketed','In Transit','At Warehouse','Out for Delivery','Delivered','Delayed','Cancelled']),
        "deliveryDate":_('datetime.datetime', start=2000, end=2024),
        "orderType": random.randint(1,10),
        "employeeId": random.randint(1,234),
        "total":_("finance.price", minimum=0.01, maximum=10000.00),
        "details": _fieldset("text.word", i=random.randint(1,30), key=lambda name:{
            "quantity": random.randint(1,10),
            "product": {}
        }),
        "customer": {},
        "shippingAddress":{},
        "store":{}


    }, iterations=100)

    customers = mdb.customers.find({},{'phones':0, 'emails':0}).to_list()
    products = mdb.products.find().to_list()
    stores = mdb.stores.find({},{'address': 0}).to_list()


    count = 0
    for x in range(0,5000):
        orders = orderSchema.create()
        with pgConn.cursor() as cursor:
            orderParams = []
            detailParams = []
            for order in orders:
                cursor.execute("Select nextval('orders_seq')")
                orderId = cursor.fetchall()
                randomCustomer = random.choice(customers)
                order['customer'] = randomCustomer
                order['store'] = random.choice(stores)
                order['shippingAddress'] = randomCustomer['address']
                orderParams.append([orderId[0][0], str(order['orderDate']), order['total'], order['store']['pg_id'], order['billingDept'], str(order['deliveryDate']), order['deliveryMethod'], order['employeeId'], str(order['fillDate']), str(order['invoiceDate']), order['invoiceId'], order['orderStatus'], order['orderType'], str(order['pickDate']), order['purchaseOrder'], order['shippingMethod'], order['shippingStatus'],order['totalPieces'], order['warehouseId'], order['weight'], order['customer']['pg_id'], order['customer']['address']['pg_id'] ])
                for detail in order['details']:
                    cursor.execute("Select nextval('order_details_seq')")
                    detailId = cursor.fetchall()
                    detail['product'] = random.choice(products)
                    detailParams.append([detailId[0][0],detail['quantity'], orderId[0][0], detail['product']['pg_id']])
                count+=1
            execute_values(cursor, ordersSQL, orderParams)
            execute_values(cursor, detailSQL, detailParams)
            pgConn.commit()
        mdb.orders.insert_many(orders)
        if count % 50000 == 0:
            print(f'Created {count} orders so far.... be patient')
    print(f'Created {count} orders')


try:
    create_customers()
    create_products()
    create_stores()
    create_orders()
except Exception as e:
    print(f'Error occured: {e}')
finally:
    print('All done')
