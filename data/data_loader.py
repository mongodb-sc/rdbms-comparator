import requests
from mimesis.locales import Locale,LocaleError
import random
from mimesis.schema import Field, Schema, Fieldset
import json



baseUrl = 'http://localhost:8080/api'
headers = {'Content-type': 'application/json'}


_ = Field(Locale.EN)
_fieldset = Fieldset(Locale.EN)

types = ['work','home','mobile']


def create_customers():
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


    for x in range(1,100):
        for cust in customerSchema.create():
            custUrl = f'{baseUrl}/customers'
            response = requests.post(custUrl, data=json.dumps(cust), headers=headers)
        print('First loop of customers created')


def create_produts():
    productSchema = Schema(schema=lambda: {
        "name": _("choice.choice", items=[ _('food.drink'), _('food.dish'), _('food.fruit')]),
        "dept": random.randint(1,100),
        "price": _("finance.price", minimum=0.01, maximum=10000.00),
        "description":_("text.text", quantity=1)

    }, iterations=100)

    for x in range(1,100):
        for product in productSchema.create():
            productUrl = f'{baseUrl}/products'
            response = requests.post(productUrl, data=json.dumps(product), headers=headers)
        print('First loop of products created')



def create_stores():
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
    for x in range(1,100):
        for store in storeSchema.create():
            storeUrl = f'{baseUrl}/stores'
            response = requests.post(storeUrl, data=json.dumps(store), headers=headers)
            #print(response.text)
        print('First loop of Stores created')


def create_orders():
    orderUrl = f'{baseUrl}/orders'
    orderSchema = Schema(schema=lambda: {
        "orderDate":_('datetime.formatted_datetime', start=2000, end=2024, fmt="%Y-%m-%dT%H:%M:%S.%f"),
        "warehouseId": random.randint(1,9999),
        "fillDate":_('datetime.formatted_datetime', start=2000, end=2024, fmt="%Y-%m-%dT%H:%M:%S.%f"),
        "purchaseOrder": _("random.generate_string_by_mask", mask='@@##@@#@@@@##'),
        "invoiceId": random.randint(1,9999),
        "invoiceDate":_('datetime.formatted_datetime', start=2000, end=2024, fmt="%Y-%m-%dT%H:%M:%S.%f"),
        "deliveryMethod":_("choice.choice", items=[ 'Door Dash','Uber','Local','Truck','Cargo Ship','Plane','Rail']),
        "weight": random.randint(1,9999),
        "totalPieces": random.randint(1,9999),
        "pickDate":_('datetime.formatted_datetime', start=2000, end=2024, fmt="%Y-%m-%dT%H:%M:%S.%f"),
        "shippingMethod":_("choice.choice", items=[ 'Drop','Freight','Carrier','USPS']),
        "billingDept": random.randint(1,9999),
        "orderStatus":_("choice.choice", items=[ 'Received','Accepted','Processed','Filling','Filled','Packed','Shipped','Cancelled']),
        "shippingStatus":_("choice.choice", items=[ 'Ticketed','In Transit','At Warehouse','Out for Delivery','Delivered','Delayed','Cancelled']),
        "deliveryDate":_('datetime.formatted_datetime', start=2000, end=2024, fmt="%Y-%m-%dT%H:%M:%S.%f"),
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
    customers = requests.get('http://localhost:8080/api/customers?db=mongodb', headers=headers).json()
    stores = requests.get('http://localhost:8080/api/stores?db=mongodb', headers=headers).json()
    products = requests.get('http://localhost:8080/api/products?db=mongodb', headers=headers).json()


    for x in range(1,5000):

        orders = orderSchema.create()
        for order in orders:

            order['customer'] = random.choice(customers)
            order['customer_id'] = order['customer']['id']
            order['store'] = random.choice(stores)
            order['store_id'] = order['store']['id']
            order['shippingAddress'] = random.choice(customers)['address']
            order['shippingAddressId'] = order['shippingAddress']['id']
            for detail in order['details']:
                detail['product'] = random.choice(products)
                detail['product_id'] = detail['product']['id']
        response = requests.post(orderUrl, data=json.dumps(orders), headers=headers)
        print(f'Created another 100 orders')


try:
    #create_customers()
    #create_produts()
    #create_stores()
    create_orders()
except Exception as e:
    print(f'Error occured: {e}')
finally:
    print('All done')