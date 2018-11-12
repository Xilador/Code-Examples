/*
File Name: Project5.cpp
Author: Cody Dunlevy
Course: CSC 402 
Date: 4/13/18
*/

#include <iostream>
#include <fstream>
#include <iomanip>
#include <string>
#include <map>
#include <vector>
#include <cstdlib>
#include <sstream>

using namespace std;
#define DIVIDER "================================================================================================="

struct ItemInfo {
	string description;
	double price;
	double weight;

	explicit ItemInfo(string desc = "", double pr = 0, double wght = 0) {
		description = desc;
		price = pr;
		weight = wght;
	}

	void ItemInfo::print() {
		int width = 16, bWidth = 55;
		char space = ' ';
		cout << left << setw(bWidth) << setfill(space) << description;
		cout << left << setw(width) << setfill(space) << price;
		cout << left << setw(width) << setfill(space) << weight << endl;
	}
};

typedef map<string, int> Order;
typedef map<const string, ItemInfo> Catalog;

void readCatalog(Catalog& catalog, const string& fileName);
// reads the input file and creates the catalog; throws a 
// runtime_error if the file cannot be opened


void printCatalog(const Catalog& catalog);
// prints the SKU, description, price, and weight of every item in 
// the catalog 


ItemInfo getItemData(const Catalog& catalog, const string& sku);
// finds a single item by SKU and returns the details as a struct;
// returns a dummy struct with the description "Item not found", 
// price 0.00, and weight 0.00 if the SKU is not in the catalog


void displayOrderItems(const Order& order, const Catalog& catalog);
// Lists the SKU, description, and quantity of each type of order item

void addItem(Order & order, const Catalog& catalog, const string& sku, int quantity);
// adds item(s) to the order; throws a logic_error if the item cannot
// be found in the catalog 

void removeItem(Order & order, const string& sku, int quantity);
// removes items(s) from the order; throws a logic_error if the item 
// cannot be found in the order 

void displayOrderSummary(const Order& order, const Catalog& catalog);
// displays the number of unique item types, the total number of 
// items, the total cost, and the total shipping weight 


int main()
{
	// construct default Order and Catalog
	Catalog catalog;
	Order order;

	// test readCatalog exception handling by opening a non-existent file

	try { readCatalog(catalog, "NullPointerFile.txt"); }
	catch (exception& e) {
		cout << e.what() << endl;
	}

	// open CatalogData.txt by calling readCatalog which populates the Catalog map 

	try { readCatalog(catalog, "CatalogData.txt"); }
	catch (exception& e) {
		cout << e.what() << endl;
	}

	// print out the entire catalog

	cout << "\n \n \nPrinting Items Found in the Catalog" << endl;
	printCatalog(catalog);

	// search for a few specific items by SKU, some found, at least one that cannot be found
	// print out the details of few items using getItemData
	cout << "\n \n \nFinding Items by SKU" << endl;
	int width = 16, bWidth = 55;
	char space = ' ';
	cout << left << setw(width) << setfill(space) << "Searched SKU";
	cout << left << setw(bWidth) << setfill(space) << "Description";
	cout << left << setw(width) << setfill(space) << "Price";
	cout << left << setw(width) << setfill(space) << "Weight" << endl;
	cout << DIVIDER << endl;
	cout << left << setw(width) << setfill(space) << "4123BLU";
	getItemData(catalog,"4123BLU").print();
	cout << left << setw(width) << setfill(space) << "93456D";
	getItemData(catalog, "93456D").print();
	cout << left << setw(width) << setfill(space) << "123456789";
	getItemData(catalog, "123456789").print();
	// Add several items to order
	 
	cout << "\n \n \nAdding Items to Order..." << endl;
	addItem(order, catalog, "4123BLU", 20);
	addItem(order, catalog, "4123GRE", 10);
	addItem(order, catalog, "93456A", 12);
	addItem(order, catalog, "576361B", 3);

	// display items in the order

	cout << "\nFinding Items by SKU" << endl;
	displayOrderItems(order, catalog);

	// remove item(s) such that no item is completely removed

	cout << "\n \n \nRemoving Items From Order..." << endl;
	removeItem(order, "4123BLU", 10);
	removeItem(order, "4123GRE", 5);

	// display items in the order to verify quantities

	cout << "\nDisplaying Items to Verify Quantities" << endl;
	displayOrderItems(order, catalog);

	// try to remove an item that is not in the order to test exception handling

	cout << "\n \n \nRemoving Item From Order That Isnt There..." << endl;
	try { removeItem(order, "123456789", 9001); }
	catch (exception& e) {
		cout << e.what() << endl;
	}


	// remove all of at least one item to make sure the order no longer shows it
	cout << "\n \n \nRemoving All of One Item..." << endl;
	removeItem(order, "93456A", 12);

	// display items in the order

	cout << "\nDisplaying Items to Verify Quantities" << endl;
	displayOrderItems(order, catalog);

	// display order summary
	cout << "\n \n \n" << endl;
	displayOrderSummary(order, catalog);

	// portable pause
	cin.get();
	return 0;
}

void readCatalog(Catalog& catalog, const string& fileName) {
	ifstream file(fileName);
	string line, val;
	stringstream ss;
	vector<string> tokens;
	if (file.good()) {
		while (getline(file, line)) {
			ss = stringstream(line);
			while (getline(ss, val, ':'))tokens.push_back(val);
			catalog[tokens[0]] = ItemInfo(tokens[1], strtod(tokens[2].c_str(), NULL), strtod(tokens[3].c_str(), NULL));
			tokens.clear();
			ss.flush();
		}
	}
	else throw runtime_error("Cannot Open File");
}
// reads the input file and creates the catalog; throws a 
// runtime_error if the file cannot be opened


void printCatalog(const Catalog& catalog) {
	Catalog::const_iterator itr;
	int width = 16, bWidth = 55;
	char space = ' ';
	cout << left << setw(width) << setfill(space) << "SKU";
	cout << left << setw(bWidth) << setfill(space) << "Description";
	cout << left << setw(width) << setfill(space) << "Unit Price";
	cout << left << setw(width) << setfill(space) << "Weight (Ibs.)" << endl;
	cout << DIVIDER << endl;
	for (itr = catalog.begin(); itr != catalog.end(); itr++) {
		cout << left << setw(width) << setfill(space) << itr->first;
		((ItemInfo)(itr->second)).print();
	}
}
// prints the SKU, description, price, and weight of every item in 
// the catalog 


ItemInfo getItemData(const Catalog& catalog, const string& sku) {
	if (catalog.find(sku) != catalog.end()) {
		return catalog.find(sku)->second;
	}
	return ItemInfo("Item not found", 0.00, 0.00);
}
// finds a single item by SKU and returns the details as a struct;
// returns a dummy struct with the description "Item not found", 
// price 0.00, and weight 0.00 if the SKU is not in the catalog


void displayOrderItems(const Order& order, const Catalog& catalog) {
	Order::const_iterator itr;
	int width = 16, bWidth = 55;
	char space = ' ';
	cout << left << setw(width) << setfill(space) << "SKU";
	cout << left << setw(bWidth) << setfill(space) << "Description";
	cout << left << setw(width) << setfill(space) << "Quantity" << endl;
	cout << DIVIDER << endl;
	for (itr = order.begin(); itr != order.end(); itr++) {
		cout << left << setw(width) << setfill(space) << itr->first;
		cout << left << setw(bWidth) << setfill(space) << getItemData(catalog, itr->first).description;
		cout << left << setw(width) << setfill(space) << itr->second << endl;

	}
}
// Lists the SKU, description, and quantity of each type of order item

void addItem(Order & order, const Catalog& catalog, const string& sku, int quantity) {
	if (catalog.find(sku) != catalog.end()) {
		if ((order.find(sku) != order.end()) && (order.find(sku)->first == sku)) order.find(sku)->second += quantity;
		else order[sku] = quantity;
	}
	else throw logic_error("Cannot Find Requested SKU");
}
// adds item(s) to the order; throws a logic_error if the item cannot
// be found in the catalog 

void removeItem(Order & order, const string& sku, int quantity) {
	if (order.find(sku) != order.end()) {
		if (order.find(sku)->second - quantity <= 0) order.erase(sku);
		else order.find(sku)->second -= quantity;
	}
	else throw logic_error("Cannot Find Requested SKU to Remove");
}
// removes items(s) from the order; throws a logic_error if the item 
// cannot be found in the order 

void displayOrderSummary(const Order& order, const Catalog& catalog) {
	Order::const_iterator itr;
	int width = 16, bWidth = 24;
	char space = ' ';
	cout << left << setw(width) << setfill(space) << "Order Summary" << endl;
	cout << DIVIDER << endl;

	int uniqueItems = 0, totalItems = 0;
	double totalCost = 0, totalWeight = 0;
	for (itr = order.begin(); itr != order.end(); itr++) {
		uniqueItems++;
		totalItems += itr->second;
		totalCost += (getItemData(catalog, itr->first).price) * (itr->second);
		totalWeight += (getItemData(catalog, itr->first).weight) * (itr->second);
	}
	cout << left << setw(bWidth) << setfill(space) << "Distinct Item Types:";
	cout << left << setw(width) << setfill(space) << uniqueItems << endl;
	cout << left << setw(bWidth) << setfill(space) << "Total Number of Items:";
	cout << left << setw(width) << setfill(space) << totalItems << endl;
	cout << left << setw(bWidth) << setfill(space) << "Total Cost ($):";
	cout << left << setw(width) << setfill(space) << totalCost << endl;
	cout << left << setw(bWidth) << setfill(space) << "Total Weight (Ibs.):";
	cout << left << setw(width) << setfill(space) << totalWeight << endl;
}
// displays the number of unique item types, the total number of 
// items, the total cost, and the total shipping weight 